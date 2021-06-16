<template>
  <a-layout>
    <a-layout-content
            :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >

        <a-row :gutter="24">
          <a-col :span="8">
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
                    v-if="level1.length > 0"
                    :columns="columns"
                    :row-key="record => record.id"
                    :data-source="level1"
                    :loading="loading"
                    :pagination="false"
                    size="small"
                    :defaultExpandAllRows="true"
            >
              <template #name="{ text, record }">
                {{record.sort}} {{text}}
              </template>

              <template v-slot:action="{ text, record }">
                <a-space size="small">
                  <a-button type="primary" @click="edit(record)" size="small">
                    Edit
                  </a-button>
                  <a-popconfirm
                          title="删除后不可恢复，确认删除?"
                          ok-text="Yes"
                          cancel-text="No"
                          @confirm="handleDelete(record.id)"
                  >
                    <a-button type="danger" size="small">
                      Delete
                    </a-button>
                  </a-popconfirm>
                </a-space>
              </template>
            </a-table>
          </a-col>
          <a-col :span="16">
            <p>
              <a-form layout="inline" :model="param">
                <a-form-item>
                  <a-button type="primary" @click="handleSave()">
                    保存
                  </a-button>
                </a-form-item>
              </a-form>
            </p>

            <a-form :model="doc" layout="vertical">
              <a-form-item>
                <a-input v-model:value="doc.name" placeholder="名称"/>
              </a-form-item>


              <a-form-item>

                <a-tree-select

                        v-model:value="doc.parent"
                        style="width: 100%"
                        :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                        :tree-data="treeSelectData"
                        placeholder="请选择父文档"
                        tree-default-expand-all
                        :replaceFields="{title: 'name', key: 'id', value: 'id'}"
                >
                </a-tree-select>
              </a-form-item>

              <a-form-item>
                <a-input v-model:value="doc.sort" placeholder="顺序"/>
              </a-form-item>

              <a-form-item>
                <a-button type="primary" @click="handlePreviewContent()">
                  <EyeOutlined /> Preview
                </a-button>
              </a-form-item>

              <a-form-item>
                <div id="content"></div>
              </a-form-item>
            </a-form>
          </a-col>
        </a-row>
      <a-drawer width="900" placement="right" :closable="false" :visible="drawerVisible" @close="onDrawerClose">
        <div class="wangeditor" :innerHTML="previewHtml"></div>
      </a-drawer>
    </a-layout-content>
  </a-layout>
</template>


<script lang="ts">
  import { defineComponent, onMounted, ref , createVNode} from 'vue';
  import axios from 'axios';
  import {message, Modal} from 'ant-design-vue';
  import { Tool } from '@/util/tool';
  import {useRoute} from "vue-router";
  import ExclamationCircleOutlined from "@ant-design/icons-vue/ExclamationCircleOutlined";
  import E from 'wangeditor';
  import i18next from "i18next";


  export default defineComponent({
    name: 'AdminDoc',
    setup() {
      const route = useRoute();
      // console.log("route：", route);
      // console.log("route.path：", route.path);
      // console.log("route.query：", route.query);
      // console.log("route.param：", route.params);
      // console.log("route.fullPath：", route.fullPath);
      // console.log("route.name：", route.name);
      // console.log("route.meta：", route.meta);
      const param = ref();
      param.value={};
      const docs = ref();
      const loading = ref(false);

      const treeSelectData = ref();
      treeSelectData.value = [];

      const columns = [
        {
          title: '名称',
          dataIndex: 'name',
          slots: { customRender: 'name' }
        },
        {
          title: 'Action',
          key: 'action',
          slots: { customRender: 'action' }
        }
      ];

      const level1 = ref();
      level1.value=[];
      /**
       * 数据查询
       **/
      const handleQuery = () => {
        loading.value = true;
        level1.value = [];
        axios.get("/doc/all/" + route.query.ebookId).then((response) => {
          loading.value = false;
          const data = response.data;
          if(data.success){
            docs.value = data.content;
            level1.value = [];
            level1.value = Tool.array2Tree(docs.value, 0);

            treeSelectData.value = Tool.copy(level1.value);
            treeSelectData.value.unshift({id:0, name:'None'});
          }else{
            message.error(data.message);
          }

        });
      };


      // -------- 表单 ---------
      /**
       * 数组，[100, 101]对应：前端开发 / Vue
       */

      const doc = ref();
      doc.value = {
        ebookId: route.query.ebookId
      };
      const editor = new E('#content');
      editor.config.zIndex = 0;
      editor.config.lang ="en";
      editor.i18next = i18next.createInstance();

      const handleSave = () => {
        doc.value.content = editor.txt.html();

        if(!doc.value.id){
          axios.post("/doc", doc.value).then((response) =>{
            const data = response.data;
            if (data.success) {
              message.success("update successfully");
              // 重新加载列表
              handleQuery();
            }
          })
        }else{
          axios.put("/doc", doc.value).then((response) => {
            const data = response.data;
            if (data.success) {
              // 重新加载列表
              handleQuery();
            } else {
              message.error(data.message);
            }
          });
        }

      };

      /**
       * 将某节点及其子孙节点全部置为disabled
       */
      const setDisable = (treeSelectData: any, id: any) => {
        // console.log(treeSelectData, id);
        // 遍历数组，即遍历某一层节点
        for (let i = 0; i < treeSelectData.length; i++) {
          const node = treeSelectData[i];
          if (node.id === id) {
            // 如果当前节点就是目标节点
            console.log("disabled", node);
            // 将目标节点设置为disabled
            node.disabled = true;

            // 遍历所有子节点，将所有子节点全部都加上disabled
            const children = node.children;
            if (Tool.isNotEmpty(children)) {
              for (let j = 0; j < children.length; j++) {
                setDisable(children, children[j].id)
              }
            }
          } else {
            // 如果当前节点不是目标节点，则到其子节点再找找看。
            const children = node.children;
            if (Tool.isNotEmpty(children)) {
              setDisable(children, id);
            }
          }
        }
      };



      const deleteIds: Array<string> = [];
      const deleteNames: Array<string> = [];
      /**
       * 查找整根树枝
       */
      const getDeleteIds = (treeSelectData: any, id: any) => {
        // console.log(treeSelectData, id);
        // 遍历数组，即遍历某一层节点
        for (let i = 0; i < treeSelectData.length; i++) {
          const node = treeSelectData[i];
          if (node.id === id) {
            // 如果当前节点就是目标节点
            console.log("delete", node);
            // 将目标ID放入结果集ids
            // node.disabled = true;
            deleteIds.push(id);
            deleteNames.push(node.name);

            // 遍历所有子节点
            const children = node.children;
            if (Tool.isNotEmpty(children)) {
              for (let j = 0; j < children.length; j++) {
                getDeleteIds(children, children[j].id)
              }
            }
          } else {
            // 如果当前节点不是目标节点，则到其子节点再找找看。
            const children = node.children;
            if (Tool.isNotEmpty(children)) {
              getDeleteIds(children, id);
            }
          }
        }
      };


      /**
       * 内容查询
       **/
      const handleQueryContent = () => {
        axios.get("/content/" + doc.value.id).then((response) => {
          const data = response.data;
          if (data.success) {
            if(data.content && data.content.content){
              editor.txt.html(data.content.content);
            }
          } else {
            message.error(data.message);
          }
        });
      };

      /**
       * 编辑
       */
      const edit = (record: any) => {
        editor.txt.html("");
        doc.value = Tool.copy(record);
        handleQueryContent();

        // 不能选择当前节点及其所有子孙节点，作为父节点，会使树断开
        treeSelectData.value = Tool.copy(level1.value);
        setDisable(treeSelectData.value, record.id);

        // 为选择树添加一个"无"
        treeSelectData.value.unshift({id: 0, name: 'None'});
      };

      /**
       * 新增
       */
      const add = () => {
        editor.txt.html("");
        doc.value = {
          ebookId : route.query.ebookId
        };

        treeSelectData.value = Tool.copy(level1.value);

        // 为选择树添加一个"无"
        treeSelectData.value.unshift({id: 0, name: '无'});
      };

      // const handleDelete = (id: number) => {
      //   getDeleteIds(level1.value, id);
      //   axios.delete("/doc/" + deleteIds.join(",")).then((response) => {
      //     const data = response.data;
      //     if (data.success) {
      //       // 重新加载列表
      //       handleQuery();
      //     } else {
      //       message.error(data.message);
      //     }
      //   });
      // };
      const handleDelete = (id: number) => {
        // console.log(level1, level1.value, id)
        // 清空数组，否则多次删除时，数组会一直增加
        deleteIds.length = 0;
        deleteNames.length = 0;
        getDeleteIds(level1.value, id);
        Modal.confirm({
          title: '重要提醒',
          icon: createVNode(ExclamationCircleOutlined),
          content: '将删除：【' + deleteNames.join("，") + "】删除后不可恢复，确认删除？",
          onOk() {
            // console.log(ids)
            axios.delete("/doc/" + deleteIds.join(",")).then((response) => {
              const data = response.data; // data = commonResp
              if (data.success) {
                // 重新加载列表
                handleQuery();
              } else {
                message.error(data.message);
              }
            });
          },
        });
      };

      // ----------------富文本预览--------------
      const drawerVisible = ref(false);
      const previewHtml = ref();
      const handlePreviewContent = () => {
        const html = editor.txt.html();
        previewHtml.value = html;
        drawerVisible.value = true;
      };
      const onDrawerClose = () => {
        drawerVisible.value = false;
      };

      onMounted(() => {
        editor.create();
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
        handleSave,
        handleDelete,
        handleQuery,
        treeSelectData,
        drawerVisible,
        previewHtml,
        handlePreviewContent,
        onDrawerClose,
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