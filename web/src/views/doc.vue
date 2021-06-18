<template>
  <a-layout>
    <a-layout-content :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }">
      <h3 v-if="level1.length === 0">Sorry, no related documents can be found！</h3>
      <a-row>
        <a-col :span="6">
          <a-tree
                  v-if="level1.length > 0"
                  :tree-data="level1"
                  @select="onSelect"
                  :replaceFields="{title: 'name', key: 'id', value: 'id'}"
                  :defaultExpandAll="true"
                  :defaultSelectedKeys="defaultSelectedKeys"
          >
          </a-tree>
        </a-col>
        <a-col :span="18">
          <div>
            <h2>{{doc.name}}</h2>
            <div>
              <span>views：{{doc.viewCount}}</span> &nbsp; &nbsp;
              <span>Votes：{{doc.voteCount}}</span>
              <a-button id="vote-btn" type="primary" shape="round" :size="'small'" @click="vote" :disabled="isVoted">
                <template #icon><LikeOutlined /> &nbsp;Vote </template>
              </a-button>
            </div>
            <a-divider style="height: 2px; background-color: #9999cc"/>
          </div>
          <div class="wangeditor" :innerHTML="html"></div>

        </a-col>
      </a-row>
    </a-layout-content>
  </a-layout>
</template>

<script lang="ts">
  import {defineComponent, onMounted, ref, createVNode, computed} from 'vue';
  import axios from 'axios';
  import {message} from 'ant-design-vue';
  import {Tool} from "@/util/tool";
  import {useRoute} from "vue-router";
  import store from "@/store";

  export default defineComponent({
    name: 'Doc',
    setup: function () {
      const curUser = computed(() => store.state.user);
      const isVoted = ref();
      const voteRequest = ref();
      voteRequest.value={};
      const route = useRoute();
      const docs = ref();
      const html = ref();
      const defaultSelectedKeys = ref();
      defaultSelectedKeys.value = [];
      // currently selected document
      const doc = ref();
      doc.value = {};

      /**
       * The first-level document tree, the children attribute is the second-level document
       * [{
       *   id: "",
       *   name: "",
       *   children: [{
       *     id: "",
       *     name: "",
       *   }]
       * }]
       */
      const level1 = ref(); //The first-level document tree, the children attribute is the second-level document
      level1.value = [];

      /**
       * Content Query
       **/
      const handleQueryContent = (id: number) => {
        axios.get("/content/" + id).then((response) => {
          const data = response.data;
          if (data.success) {
            if (data.content && data.content.content) {
              html.value = data.content.content;
              doc.value.viewCount++;
            } else {
              html.value = "";
            }
            checkIsVoted(id, curUser.value.id);
          } else {
            message.error(data.message);
          }
        });
      };

      /**
       * Data Query
       **/
      const handleQuery = () => {
        axios.get("/doc/all/" + route.query.ebookId).then((response) => {
          const data = response.data;
          if (data.success) {
            docs.value = data.content;

            level1.value = [];
            level1.value = Tool.array2Tree(docs.value, 0);

            if (Tool.isNotEmpty(level1)) {
              defaultSelectedKeys.value = [level1.value[0].id];
              handleQueryContent(level1.value[0].id);

              doc.value = level1.value[0];
            }
          } else {
            message.error(data.message);
          }
        });
      };

      const checkIsVoted = (docId: any, userId: any) => {
        if(!Tool.isEmpty(userId)){
          axios.get("/doc/vote?userId="+userId+"&docId="+docId).then((response) => {
            const data = response.data;
            if (data.success) {
              isVoted.value = data.content;
            } else {
              message.error(data.message);
            }
          });
        }
      }

      const onSelect = (selectedKeys: any, info: any) => {
        console.log('selected', selectedKeys, info);
        if (Tool.isNotEmpty(selectedKeys)) {
          //When a node is selected, load the document information of the node
          doc.value = info.selectedNodes[0].props;
          handleQueryContent(selectedKeys[0]);
        }
      };

      // vote
      const vote = () => {
        voteRequest.value = {
          docId: doc.value.id,
          userId: curUser.value.id
        }
        axios.post('/doc/vote', voteRequest.value).then((response) => {
          const data = response.data;
          if (data.success) {
            doc.value.voteCount++;
          } else {
            message.error(data.message);
          }
          isVoted.value = true;
        });
      };

      onMounted(() => {
        handleQuery();
      });

      return {
        level1,
        html,
        onSelect,
        defaultSelectedKeys,
        doc,
        vote,
        isVoted
      }
    }
  });
</script>

<style>
  /* refer: http://www.wangeditor.com/doc/pages/02-%E5%86%85%E5%AE%B9%E5%A4%84%E7%90%86/03-%E8%8E%B7%E5%8F%96html.html */
  /* table style */
  .wangeditor table {
    border-top: 1px solid #ccc;
    border-left: 1px solid #ccc;
  }
  .wangeditor table td,
  .wangeditor table th {
    border-bottom: 1px solid #ccc;
    border-right: 1px solid #ccc;
    padding: 3px 5px;
  }
  .wangeditor table th {
    border-bottom: 2px solid #ccc;
    text-align: center;
  }

  /* blockquote style */
  .wangeditor blockquote {
    display: block;
    border-left: 8px solid #d0e5f2;
    padding: 5px 10px;
    margin: 10px 0;
    line-height: 1.4;
    font-size: 100%;
    background-color: #f1f1f1;
  }

  /* code style */
  .wangeditor code {
    display: inline-block;
    *display: inline;
    *zoom: 1;
    background-color: #f1f1f1;
    border-radius: 3px;
    padding: 3px 5px;
    margin: 0 3px;
  }
  .wangeditor pre code {
    display: block;
  }

  /* ul ol style */
  .wangeditor ul, ol {
    margin: 10px 0 10px 20px;
  }

  /* Conflict with antdv p, overwrite */
  .wangeditor blockquote p {
    font-family:"YouYuan";
    margin: 20px 10px !important;
    font-size: 16px !important;
    font-weight:600;
  }

  /* Vote */
  #vote-btn {
    text-align: center;
    float: right;
  }

  /* Picture adaptive */
  .wangeditor img {
    max-width: 100%;
    height: auto;
  }

  /*  Video adaptive */
  .wangeditor iframe {
    width: 100%;
    height: 400px;
  }
</style>


