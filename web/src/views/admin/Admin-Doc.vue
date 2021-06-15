<template>
  <a-layout>
    <a-layout-content
            :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <p>
        <a-form layout="inline" :model="param">

          <a-form-item>
            <a-button type="primary" @click="handleQuery()">
              查询
            </a-button>
          </a-form-item>
          <a-form-item>
            <a-button type="primary" @click="add()">
              新增
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
                    title="删除后不可恢复，确认删除?"
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
          title="文档表单"
          v-model:visible="modalVisible"
          :confirm-loading="modalLoading"
          @ok="handleModalOk"
  >    <a-form :model="doc" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
    <a-form-item label="名称">
      <a-input v-model:value="doc.name" />
    </a-form-item>
    <a-form-item label="父分类">
      <a-select
              v-model:value="doc.parent"
              ref="select"
      >
        <a-select-option :value="0">
          None
        </a-select-option>
        <a-select-option v-for="c in level1" :key="c.id" :value="c.id" :disabled="doc.id === c.id">
          {{c.name}}
        </a-select-option>
      </a-select>
    </a-form-item>
    <a-form-item label="顺序">
      <a-input v-model:value="doc.sort" />
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
    name: 'AdminDoc',
    setup() {
      const param = ref();
      param.value={};
      const docs = ref();
      const loading = ref(false);

      const columns = [
        {
          title: '名称',
          dataIndex: 'name'
        },
        {
          title: '父分类',
          key: 'parent',
          dataIndex: 'parent'
        },
        {
          title: '顺序',
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
       * 数据查询
       **/
      const handleQuery = () => {
        loading.value = true;
        docs.value = [];
        axios.get("/doc").then((response) => {
          loading.value = false;
          const data = response.data;
          if(data.success){
            docs.value = data.content;
            level1.value = [];
            level1.value = Tool.array2Tree(docs.value, 0);
          }else{
            message.error(data.message);
          }

        });
      };


      // -------- 表单 ---------
      /**
       * 数组，[100, 101]对应：前端开发 / Vue
       */
      // const docIds = ref();
      const doc = ref();
      const modalVisible = ref(false);
      const modalLoading = ref(false);
      const handleModalOk = () => {
        modalLoading.value = true;


        if(!doc.value.id){
          axios.post("/doc", doc.value).then((response) =>{
            modalLoading.value = false;
            const data = response.data;
            if (data.success) {
              modalVisible.value = false;

              // 重新加载列表
              handleQuery();
            }
          })
        }else{
          axios.put("/doc", doc.value).then((response) => {
            modalLoading.value = false;
            const data = response.data;
            if (data.success) {
              modalVisible.value = false;

              // 重新加载列表
              handleQuery();
            } else {
              message.error(data.message);
            }
          });
        }

      };


      /**
       * 编辑
       */
      const edit = (record: any) => {
        modalVisible.value = true;
        doc.value = Tool.copy(record);
        // docIds.value = [doc.value.doc1Id, doc.value.doc2Id]
      };

      /**
       * 新增
       */
      const add = () => {
        modalVisible.value = true;
        doc.value = {};
      };

      const handleDelete = (id: number) => {
        axios.delete("/doc/" + id).then((response) => {
          const data = response.data;
          if (data.success) {
            // 重新加载列表
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
        doc,
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