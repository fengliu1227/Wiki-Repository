<template>
  <a-layout>
    <a-layout-content
            :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <p>
        <a-form layout="inline" :model="param">
          <a-form-item>
            <a-input v-model:value="param.name" placeholder="Login name">
            </a-input>
          </a-form-item>
          <a-form-item>
            <a-button type="primary" @click="handleQuery({page: 1, size: pagination.pageSize})">
              Search
            </a-button>
          </a-form-item>
          <a-form-item>
            <a-button type="primary" @click="add()">
              Add
            </a-button>
          </a-form-item>
        </a-form>
      </p>
      <a-table
              :columns="columns"
              :row-key="record => record.id"
              :data-source="ebooks"
              :pagination="pagination"
              :loading="loading"
              @change="handleTableChange"
      >
        <template #cover="{ text: cover }">
          <img v-if="cover" :src="cover" alt="avatar" />
        </template>

        <template v-slot:category="{ text, record }">
          <span>{{ getCategoryName(record.category1Id) }} / {{ getCategoryName(record.category2Id) }}</span>
        </template>
        <template v-slot:action="{ text, record }">
          <a-space size="small">

            <router-link :to="'/admin/doc?ebookId=' + record.id">
              <a-button type="primary">
                Doc mgmt.
              </a-button>
            </router-link>

            <a-button type="primary" @click="edit(record)">
              Edit
            </a-button>
            <a-popconfirm
                    title="Unrecoverable after deletion, Are you sure to delete？"
                    ok-text="Yes"
                    cancel-text="No"
                    @confirm="handleDelete(record.id)"
            >
              <a-button type="danger">
                Delete
              </a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </a-table>
    </a-layout-content>
  </a-layout>

  <a-modal
          title="Ebook form"
          v-model:visible="modalVisible"
          :confirm-loading="modalLoading"
          @ok="handleModalOk"
  ><a-form :model="ebook" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
    <a-form-item label="Cover">
      <a-input v-model:value="ebook.cover" />
    </a-form-item>
    <a-form-item label="Name">
      <a-input v-model:value="ebook.name" />
    </a-form-item>
    <a-form-item label="Category">
      <a-cascader
              v-model:value="categoryIds"
              :field-names="{ label: 'name', value: 'id', children: 'children' }"
              :options="level1"
      />
    </a-form-item>
    <a-form-item label="Description">
      <a-input v-model:value="ebook.description" type="textarea" />
    </a-form-item>
  </a-form>
  </a-modal>
</template>


<script lang="ts">
  import { defineComponent, onMounted, ref } from 'vue';
  import axios from 'axios';
  import { message } from 'ant-design-vue';
  import { Tool } from '@/util/tool';


  export default defineComponent({
    name: 'AdminEbook',
    setup() {
      const param = ref();
      param.value={};
      const ebooks = ref();
      const pagination = ref({
        current: 1,
        pageSize: 5,
        total: 0
      });
      const loading = ref(false);

      const columns = [
        {
          title: 'Cover',
          dataIndex: 'cover',
          slots: { customRender: 'cover' }
        },
        {
          title: 'Name',
          dataIndex: 'name'
        },
        {
          title: 'Category',
          slots: { customRender: 'category' }
        },
        {
          title: 'Doc',
          dataIndex: 'docCount'
        },
        {
          title: 'View',
          dataIndex: 'viewCount'
        },
        {
          title: 'Vote',
          dataIndex: 'voteCount'
        },
        {
          title: 'Action',
          key: 'action',
          slots: { customRender: 'action' }
        }
      ];

      /**
       * data query
       **/
      const handleQuery = (params: any) => {
        loading.value = true;
        ebooks.value = [];
        axios.get("/ebook/list", {
          params:{
            page: params.page,
            size: params.size,
            name:param.value.name,
          }
        }).then((response) => {
          loading.value = false;
          const data = response.data;
          if(data.success){
            ebooks.value = data.content.list;
            pagination.value.current = params.page;
            pagination.value.total = data.content.total;
          }else{
            message.error(data.message);
          }

        });
      };

      /**
       * Triggered when the table clicks on the page number
       */
      const handleTableChange = (pagination: any) => {
        handleQuery({
          page: pagination.current,
          size: pagination.pageSize
        });
      };

      // -------- Form ---------
      const categoryIds = ref();
      const ebook = ref();
      const modalVisible = ref(false);
      const modalLoading = ref(false);
      const handleModalOk = () => {
        modalLoading.value = true;
        ebook.value.category1Id = categoryIds.value[0];
        ebook.value.category2Id = categoryIds.value[1];
        if(!ebook.value.id){
          axios.post("/ebook", ebook.value).then((response) =>{
            modalLoading.value = false;
            const data = response.data;
            if (data.success) {
              modalVisible.value = false;

              handleQuery({
                page: pagination.value.current,
                size: pagination.value.pageSize,
              });
            }
          })
        }else{
          axios.put("/ebook", ebook.value).then((response) => {
            modalLoading.value = false;
            const data = response.data;
            if (data.success) {
              modalVisible.value = false;

              handleQuery({
                page: pagination.value.current,
                size: pagination.value.pageSize,
              });
            } else {
              message.error(data.message);
            }
          });
        }
      };


      /**
       * edit
       */
      const edit = (record: any) => {
        modalVisible.value = true;
        ebook.value = Tool.copy(record);
        categoryIds.value = [ebook.value.category1Id, ebook.value.category2Id]
      };

      /**
       * add
       */
      const add = () => {
        modalVisible.value = true;
        ebook.value = {};
      };

      const handleDelete = (id: number) => {
        axios.delete("/ebook/" + id).then((response) => {
          const data = response.data; // data = commonResp
          if (data.success) {
            handleQuery({
              page: pagination.value.current,
              size: pagination.value.pageSize,
            });
          } else {
            message.error(data.message);
          }
        });
      };

      const level1 =  ref();
      let categorys: any;
      /**
       * Category Query
       **/
      const handleQueryCategory = () => {
        loading.value = true;
        axios.get("/category").then((response) => {
          loading.value = false;
          const data = response.data;
          if (data.success) {
            categorys = data.content;

            level1.value = [];
            level1.value = Tool.array2Tree(categorys, 0);

            handleQuery({
              page: 1,
              size: pagination.value.pageSize,
            });
          } else {
            message.error(data.message);
          }
        });
      };

      const getCategoryName = (cid: number) => {
        // console.log(cid)
        let result = "";
        categorys.forEach((item: any) => {
          if (item.id === cid) {
            // return item.name; // return doesn't work here
            result = item.name;
          }
        });
        return result;
      };

      onMounted(() => {
        handleQueryCategory()
      });

      return {
        ebooks,
        pagination,
        columns,
        loading,
        param,
        handleTableChange,
        edit,
        add,
        ebook,
        modalVisible,
        modalLoading,
        handleModalOk,
        handleDelete,
        handleQuery,
        categoryIds,
        level1,
        getCategoryName,
      }
    }
  });
</script>
