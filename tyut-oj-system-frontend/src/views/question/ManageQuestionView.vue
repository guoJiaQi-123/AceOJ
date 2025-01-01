<template>
  <div id="manageQuestionView">
    <h1>题目管理</h1>
    <a-table
      :columns="columns"
      :data="dataList"
      :pagination="{
        pageSize: searchParams.pageSize,
        current: searchParams.current,
        showTotal: true,
        total: total,
      }"
      @page-change="pageChange"
    >
      <template #tags="{ record }">
        <a-space wrap v-for="(tag, index) of record.tags">
          <span></span>
          <span v-if="tag == '简单'">
            <a-tag :key="index" color="#00b42a"> {{ tag }} </a-tag>
          </span>
          <span v-else-if="tag == '中等'">
            <a-tag :key="index" color="#ffb400">
              {{ tag }}
            </a-tag>
          </span>
          <span v-else-if="tag == '困难'">
            <a-tag :key="index" color="#f53f3f">
              {{ tag }}
            </a-tag>
          </span>
          <span v-else-if="tag == '栈'">
            <a-tag :key="index" color="#168cff">
              {{ tag }}
            </a-tag>
          </span>
          <span v-else>
            <a-tag :key="index" color="#ff7d00">
              {{ tag }}
            </a-tag>
          </span>
        </a-space>
      </template>
      <template #optional="{ record }">
        <a-space>
          <a-button type="primary" @click="doUpdate(record)">修改</a-button>
          <a-button type="primary" status="danger" @click="doDelete(record)"
            >删除
          </a-button>
        </a-space>
      </template>
      <template #createTime="{ record }">
        {{ moment(record.createTime).format("YYYY-MM-DD") }}
      </template>
    </a-table>
  </div>
</template>
<script setup lang="ts">
import { onMounted, ref, watchEffect } from "vue";
import { Question, QuestionControllerService } from "@/generated";
import { Message } from "@arco-design/web-vue";
import { useRouter } from "vue-router";
import moment from "moment/moment";

const total = ref(0);
const router = useRouter();
const dataList = ref([]);
const searchParams = ref({
  pageSize: 10,
  current: 1,
});
const loadData = async () => {
  const res = await QuestionControllerService.listQuestionVoByPageUsingPost(
    searchParams.value
  );
  if (res.code === 0) {
    dataList.value = res.data.records;
    total.value = res.data.total;
  } else {
    Message.error("加载失败，" + res.message);
    console.log(res.message);
  }
};

watchEffect(() => {
  loadData();
});
onMounted(() => {
  loadData();
});
const columns = [
  {
    title: "题目ID",
    dataIndex: "id",
  },
  {
    title: "标题",
    dataIndex: "title",
  },
  // {
  //   title: "内容",
  //   dataIndex: "content",
  // },
  {
    title: "题目标签",
    slotName: "tags",
  },
  // {
  //   title: "答案",
  //   dataIndex: "answer",
  // },
  {
    title: "提交数",
    dataIndex: "submitNum",
  },
  {
    title: "通过数",
    dataIndex: "acceptedNum",
  },
  // {
  //   title: "判题配置",
  //   dataIndex: "judgeConfig",
  // },
  // {
  //   title: "判题用例",
  //   dataIndex: "judgeCase",
  // },

  {
    title: "创建人ID",
    dataIndex: "userId",
  },
  {
    title: "创建时间",
    slotName: "createTime",
  },
  {
    title: "操作",
    slotName: "optional",
  },
];
const doDelete = async (question: Question) => {
  if (confirm("确定要删除吗？")) {
    const res = await QuestionControllerService.deleteQuestionUsingPost({
      id: question.id,
    });
    if (res.code === 0) {
      Message.success("删除成功");
      await loadData();
    } else {
      Message.error("操作失败" + res.message);
    }
  }
};
const pageChange = (page: number) => {
  searchParams.value = {
    ...searchParams.value,
    current: page,
  };
};
const doUpdate = (question: Question) => {
  router.push({
    path: "/update/question",
    query: {
      id: question.id,
    },
  });
};
</script>
<style scoped>
#manageQuestionView {
  max-width: 1480px;
  margin: 0 auto;
}
</style>
