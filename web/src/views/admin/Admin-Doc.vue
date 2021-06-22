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
                          title="Unrecoverable after deletion, Are you sure to delete？"
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
                    Save
                  </a-button>
                </a-form-item>
              </a-form>
            </p>

            <a-form :model="doc" layout="vertical">
              <a-form-item>
                <a-input v-model:value="doc.name" placeholder="Name"/>
              </a-form-item>


              <a-form-item>

                <a-tree-select

                        v-model:value="doc.parent"
                        style="width: 100%"
                        :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                        :tree-data="treeSelectData"
                        placeholder="please select parent category"
                        tree-default-expand-all
                        :replaceFields="{title: 'name', key: 'id', value: 'id'}"
                >
                </a-tree-select>
              </a-form-item>

              <a-form-item>
                <a-input v-model:value="doc.sort" placeholder="sort"/>
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
          title: 'Name',
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
       * data Query
       **/
      const handleQuery = () => {
        editor.txt.html("");
        loading.value = true;
        level1.value = [];
        axios.get("/doc/all/" + route.query.ebookId).then((response) => {
          loading.value = false;
          const data = response.data;
          if(data.success){
            docs.value = data.content;
            level1.value = [];
            level1.value = Tool.array2Tree(docs.value, 0);


            if(Tool.isEmpty(level1.value)){
              treeSelectData.value=[];
              treeSelectData.value.unshift({id:0, name:'None'});
            }else{
              treeSelectData.value = Tool.copy(level1.value);
              treeSelectData.value.unshift({id:0, name:'None'});
            }
          }else{
            message.error(data.message);
          }

        });
      };


      // -------- Form ---------
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
              // reload
              handleQuery();
            }
          })
        }else{
          axios.put("/doc", doc.value).then((response) => {
            const data = response.data;
            if (data.success) {
              // reload
              handleQuery();
            } else {
              message.error(data.message);
            }
          });
        }

      };

      /**
       * Set a node and its descendants to disabled
       */
      const setDisable = (treeSelectData: any, id: any) => {
        // console.log(treeSelectData, id);
        //Traverse the array, that is, traverse a certain layer of nodes
        for (let i = 0; i < treeSelectData.length; i++) {
          const node = treeSelectData[i];
          if (node.id === id) {
            // If the current node is the target node
            console.log("disabled", node);
            // Set the target node to disabled
            node.disabled = true;

            // Traverse all child nodes and add disabled to all child nodes
            const children = node.children;
            if (Tool.isNotEmpty(children)) {
              for (let j = 0; j < children.length; j++) {
                setDisable(children, children[j].id)
              }
            }
          } else {
            // If the current node is not the target node, go to its child nodes and look for it again
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
       * Find the whole branch
       */
      const getDeleteIds = (treeSelectData: any, id: any) => {
        // console.log(treeSelectData, id);
        // Traverse the array, that is, traverse a certain layer of nodes
        for (let i = 0; i < treeSelectData.length; i++) {
          const node = treeSelectData[i];
          if (node.id === id) {
            // If the current node is the target node
            console.log("delete", node);
            // Put the target ID into the result set ids
            // node.disabled = true;
            deleteIds.push(id);
            deleteNames.push(node.name);

            // Traverse all child nodes
            const children = node.children;
            if (Tool.isNotEmpty(children)) {
              for (let j = 0; j < children.length; j++) {
                getDeleteIds(children, children[j].id)
              }
            }
          } else {
            // If the current node is not the target node, go to its child nodes and look for it again.
            const children = node.children;
            if (Tool.isNotEmpty(children)) {
              getDeleteIds(children, id);
            }
          }
        }
      };


      /**
       * Content Query
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
       * edit
       */
      const edit = (record: any) => {
        editor.txt.html("");
        doc.value = Tool.copy(record);
        handleQueryContent();

        // treeSelectData.value = Tool.copy(level1.value);
        // setDisable(treeSelectData.value, record.id);
        //
        // treeSelectData.value.unshift({id: 0, name: 'None'});
        if(Tool.isEmpty(level1.value)){
          treeSelectData.value = [];
          treeSelectData.value.unshift({id: 0, name: 'None'});
        }else{
          treeSelectData.value = Tool.copy(level1.value);
          setDisable(treeSelectData.value, record.id);
          treeSelectData.value.unshift({id:0, name:'None'});
        }
      };

      /**
       * Add
       */
      const add = () => {
        editor.txt.html("");
        doc.value = {
          ebookId : route.query.ebookId
        };


        if(Tool.isEmpty(level1.value)){
          treeSelectData.value = [];
          treeSelectData.value.unshift({id: 0, name: 'None'});
        }else{
          treeSelectData.value = Tool.copy(level1.value);
          treeSelectData.value.unshift({id:0, name:'None'});
        }
      };


      const handleDelete = (id: number) => {

        deleteIds.length = 0;
        deleteNames.length = 0;
        getDeleteIds(level1.value, id);
        Modal.confirm({
          title: 'Important Notice',
          icon: createVNode(ExclamationCircleOutlined),
          content: 'Deleting：【' + deleteNames.join("，") + "】"+ "Cannot be restored after deletion, confirm the deletion?",
          onOk() {
            // console.log(ids)
            axios.delete("/doc/" + deleteIds.join(",")).then((response) => {
              const data = response.data; // data = commonResp
              if (data.success) {
                handleQuery();
              } else {
                message.error(data.message);
              }
            });
          },
        });
      };

      // ----------------Rich text preview--------------
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