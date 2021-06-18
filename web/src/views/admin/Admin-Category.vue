<template>
  <a-layout>
    <a-layout-content
            :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <p>
        <a-form layout="inline" :model="param">

          <a-form-item>
            <a-button type="primary" @click="handleQuery()">
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
              :data-source="level1"
              :loading="loading"
              :pagination="false"
      >
        <template #cover="{ text: cover }">
          <img v-if="cover" :src="cover" alt="avatar" />
        </template>

        <template v-slot:action="{ text, record }">
          <a-space size="small">
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
          title="Classification form"
          v-model:visible="modalVisible"
          :confirm-loading="modalLoading"
          @ok="handleModalOk"
  >    <a-form :model="category" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
    <a-form-item label="Name">
      <a-input v-model:value="category.name" />
    </a-form-item>
    <a-form-item label="Parent category">
      <a-select
              v-model:value="category.parent"
              ref="select"
      >
        <a-select-option :value="0">
          None
        </a-select-option>
        <a-select-option v-for="c in level1" :key="c.id" :value="c.id" :disabled="category.id === c.id">
          {{c.name}}
        </a-select-option>
      </a-select>
    </a-form-item>
    <a-form-item label="Sort">
      <a-input v-model:value="category.sort" />
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
    name: 'AdminCategory',
    setup() {
      const param = ref();
      param.value={};
      const categorys = ref();
      const loading = ref(false);

      const columns = [
        {
          title: 'Name',
          dataIndex: 'name'
        },
        {
          title: 'Parent category',
          key: 'parent',
          dataIndex: 'parent'
        },
        {
          title: 'Sort',
          dataIndex: 'sort'
        },
        {
          title: 'Action',
          key: 'action',
          slots: { customRender: 'action' }
        }
      ];

      const level1 = ref();
      /**
       * data query
       **/
      const handleQuery = () => {
        loading.value = true;
        categorys.value = [];
        axios.get("/category").then((response) => {
          loading.value = false;
          const data = response.data;
          if(data.success){
            categorys.value = data.content;
            level1.value = [];
            level1.value = Tool.array2Tree(categorys.value, 0);
          }else{
            message.error(data.message);
          }

        });
      };


      // -------- Form ---------
      /**
       * Array, [100, 101] corresponds to: front-end development / Vue
       */
      // const categoryIds = ref();
      const category = ref();
      const modalVisible = ref(false);
      const modalLoading = ref(false);
      const handleModalOk = () => {
        modalLoading.value = true;


        if(!category.value.id){
          axios.post("/category", category.value).then((response) =>{
            modalLoading.value = false;
            const data = response.data;
            if (data.success) {
              modalVisible.value = false;

              // reload
              handleQuery();
            }
          })
        }else{
          axios.put("/category", category.value).then((response) => {
            modalLoading.value = false;
            const data = response.data;
            if (data.success) {
              modalVisible.value = false;

              // reload
              handleQuery();
            } else {
              message.error(data.message);
            }
          });
        }

      };


      /**
       * EDIT
       */
      const edit = (record: any) => {
        modalVisible.value = true;
        category.value = Tool.copy(record);
        // categoryIds.value = [category.value.category1Id, category.value.category2Id]
      };

      /**
       * ADD
       */
      const add = () => {
        modalVisible.value = true;
        category.value = {};
      };

      const handleDelete = (id: number) => {
        axios.delete("/category/" + id).then((response) => {
          const data = response.data;
          if (data.success) {
            // reload
            handleQuery();
          } else {
            message.error(data.message);
          }
        });
      };


      onMounted(() => {
        handleQuery();
      });

      return {
        level1,
        columns,
        loading,
        param,
        edit,
        add,
        category,
        modalVisible,
        modalLoading,
        handleModalOk,
        handleDelete,
        handleQuery,
      }
    }
  });
</script>

<style scoped>
  img {
    width: 50px;
    height: 50px;
  }
</style>