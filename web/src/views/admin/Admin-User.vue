<template>
  <a-layout>
    <a-layout-content
            :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <p>
        <a-form layout="inline" :model="param">
          <a-form-item>
            <a-input v-model:value="param.loginName" placeholder="LoginName">
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
              :data-source="users"
              :pagination="pagination"
              :loading="loading"
              @change="handleTableChange"
      >
        <template v-slot:action="{ text, record }">
          <a-space size="small">
            <a-button type="primary" @click="resetPassword(record)">
              reset password
            </a-button>
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
          title="User From"
          v-model:visible="modalVisible"
          :confirm-loading="modalLoading"
          @ok="handleModalOk"
  >
    <a-form :model="user" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="LoginName">
        <a-input v-model:value="user.loginName" :disabled="!!user.id"/>
      </a-form-item>
      <a-form-item label="Name">
        <a-input v-model:value="user.name" />
      </a-form-item>
      <a-form-item label="Password" v-show="!user.id">
        <a-input v-model:value="user.password" type="password"/>
      </a-form-item>
    </a-form>
  </a-modal>

  <a-modal
          title="Reset Password"
          v-model:visible="resetModalVisible"
          :confirm-loading="resetModalLoading"
          @ok="handleResetModalOk"
  >
    <a-form :model="user" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="new password">
        <a-input v-model:value="user.password" type="password"/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script lang="ts">
  import { defineComponent, onMounted, ref } from 'vue';
  import axios from 'axios';
  import { message } from 'ant-design-vue';
  import {Tool} from '@/util/tool';
  const hexMd5 = require('js-md5');


  export default defineComponent({
    name: 'AdminUser',
    setup() {
      const param = ref();
      param.value = {};
      const users = ref();
      const pagination = ref({
        current: 1,
        pageSize: 10,
        total: 0
      });
      const loading = ref(false);

      const columns = [
        {
          title: 'LoginName',
          dataIndex: 'loginName'
        },
        {
          title: 'Name',
          dataIndex: 'name'
        },
        {
          title: 'Password',
          dataIndex: 'password'
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
        users.value = [];
        axios.get("/user/list", {
          params: {
            page: params.page,
            size: params.size,
            loginName: param.value.loginName
          }
        }).then((response) => {
          loading.value = false;
          const data = response.data;
          if (data.success) {
            users.value = data.content.list;

            pagination.value.current = params.page;
            pagination.value.total = data.content.total;
          } else {
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
      const user = ref();
      const modalVisible = ref(false);
      const modalLoading = ref(false);
      const handleModalOk = () => {
        modalLoading.value = true;
        if(!user.value.id){
          user.value.password = hexMd5(user.value.password );
          axios.post("/user", user.value).then((response) =>{
            modalLoading.value = false;
            const data = response.data;
            if (data.success) {
              modalVisible.value = false;

              handleQuery({
                page: pagination.value.current,
                size: pagination.value.pageSize,
              });
            }else {
              message.error(data.message);
            }
          })
        }else{
          axios.put("/user", user.value).then((response) => {
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
        user.value = Tool.copy(record);
      };

      /**
       * add
       */
      const add = () => {
        modalVisible.value = true;
        user.value = {};
      };

      const handleDelete = (id: number) => {
        axios.delete("/user/" + id).then((response) => {
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

      // -------- reset password ---------
      const resetModalVisible = ref(false);
      const resetModalLoading = ref(false);
      const handleResetModalOk = () => {
        resetModalLoading.value = true;

        user.value.password = hexMd5(user.value.password);
        axios.put("/user/" + user.value.id + "/password", user.value).then((response) => {
          resetModalLoading.value = false;
          const data = response.data; // data = commonResp
          if (data.success) {
            resetModalVisible.value = false;

            handleQuery({
              page: pagination.value.current,
              size: pagination.value.pageSize,
            });
          } else {
            message.error(data.message);
          }
        });
      };

      /**
       * reset password
       */
      const resetPassword = (record: any) => {
        resetModalVisible.value = true;
        user.value = Tool.copy(record);
        user.value.password = null;
      };

      onMounted(() => {
        handleQuery({
          page: 1,
          size: pagination.value.pageSize,
        });
      });

      return {
        param,
        users,
        pagination,
        columns,
        loading,
        handleTableChange,
        handleQuery,

        edit,
        add,

        user,
        modalVisible,
        modalLoading,
        handleModalOk,

        handleDelete,

        resetModalVisible,
        resetModalLoading,
        handleResetModalOk,
        resetPassword
      }
    }
  });
</script>