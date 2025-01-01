<template>
  <div id="questionSubmitView">
    <h1>首页</h1>
    <a-form :model="searchParams" layout="inline">
      <a-form-item field="questionId" label="题号" style="min-width: 240px">
        <a-input v-model="searchParams.questionId" placeholder="请输入名称" />
      </a-form-item>
      <a-form-item field="language" label="编程语言" style="min-width: 240px">
        <a-select
          :style="{ width: '320px' }"
          placeholder="请选择编程语言"
          v-model="searchParams.language"
        >
          <a-option>java</a-option>
          <a-option>cpp</a-option>
          <a-option>go</a-option>
          <a-option>html</a-option>
        </a-select>
      </a-form-item>
      <a-form-item>
        <a-button @click="doSubmit()" type="primary">搜索</a-button>
      </a-form-item>
    </a-form>
    <a-divider size="0"></a-divider>
    <a-table
      :columns="columns"
      :data="dataList"
      :pagination="{
        pageSize: searchParams.pageSize,
        current: searchParams.current,
        showTotal: true,
        total,
      }"
      @page-change="onPageChange"
    >
      <template #status="{ record }">
        <span></span>
        <span v-if="record.status == 0">
          <a-tag color="#f53f3f"> 等待中 </a-tag>
        </span>
        <span v-else-if="record.status == 1">
          <a-tag color="#ffb400"> 判题中 </a-tag>
        </span>
        <span v-else-if="record.status == 2">
          <a-tag color="#00b42a"> 成功 </a-tag>
        </span>
        <span v-else-if="record.status == 3">
          <a-tag color="#168cff"> 失败 </a-tag>
        </span>
      </template>
      <template #memory="{ record }">
        <span></span>
        <span v-if="record.judgeInfo.memory !== null">
          <a-tag color="#00b42a">
            {{ record.judgeInfo.memory }}
          </a-tag>
        </span>
        <span v-else>
          <a-tag color="#f53f3f">
            {{ JSON.stringify(record.judgeInfo.memory) }}</a-tag
          >
        </span>
      </template>
      <template #time="{ record }">
        <span></span>
        <span v-if="record.judgeInfo.time !== null">
          <a-tag color="#00b42a">
            {{ record.judgeInfo.time }}
          </a-tag>
        </span>
        <span v-else>
          <a-tag color="#f53f3f">
            {{ JSON.stringify(record.judgeInfo.time) }}
          </a-tag>
        </span>
      </template>
      <template #ac="{ record }">
        <span></span>
        <span v-if="record.judgeInfo.message == 'Accepted'">
          <a-tag color="#00b42a"> Accepted </a-tag>
        </span>
        <span v-else-if="record.judgeInfo.message == 'Wrong Answer'">
          <a-tag color="#f53f3f"> Wrong Answer </a-tag>
        </span>
        <span v-else-if="record.judgeInfo.message == 'Compile Error'">
          <a-tag color="#f53f3f"> Compile Error </a-tag>
        </span>
        <span v-else-if="record.judgeInfo.message == 'Memory Limit Exceeded'">
          <a-tag color="#f53f3f"> Memory Limit Exceeded </a-tag>
        </span>
        <span v-else-if="record.judgeInfo.message == 'Time Limit Exceeded'">
          <a-tag color="#f53f3f"> Time Limit Exceeded </a-tag>
        </span>
      </template>
      <template #createTime="{ record }">
        {{ moment(record.createTime).format("YYYY-MM-DD") }}
      </template>
      <template #language="{ record }">
        <span></span>
        <span v-if="record.language == 'java'">
          <a-tag color="#1456F0"> {{ record.language }} </a-tag>
        </span>
      </template>
    </a-table>
  </div>
</template>
<script setup lang="ts">
import { onMounted, ref, watchEffect } from "vue";
import {
  Question,
  QuestionControllerService,
  QuestionSubmitQueryRequest,
} from "@/generated";
import { Message } from "@arco-design/web-vue";
import { useRouter } from "vue-router";
import moment from "moment";

const router = useRouter();
const dataList = ref([]);
const total = ref(0);
const searchParams = ref<QuestionSubmitQueryRequest>({
  questionId: undefined,
  language: undefined,
  pageSize: 10,
  current: 1,
});
const loadData = async () => {
  const res = await QuestionControllerService.listQuestionSubmitByPageUsingPost(
    {
      ...searchParams.value,
      sortField: "createTime",
      sortOrder: "descend",
    }
  );
  if (res.code === 0) {
    dataList.value = res.data.records;
    total.value = res.data.total;
  } else {
    Message.error("加载失败" + res.message);
  }
};
const show = ref(true);
onMounted(() => {
  loadData();
});
const columns = [
  {
    title: "提交号",
    dataIndex: "id",
  },
  {
    title: "编程语言",
    slotName: "language",
  },
  {
    title: "是否通过",
    dataIndex: "judgeInfo",
    slotName: "ac",
  },
  {
    title: "内存消耗",
    dataIndex: "judgeInfo",
    slotName: "memory",
  },
  {
    title: "时间消耗",
    dataIndex: "judgeInfo",
    slotName: "time",
  },
  {
    title: "判题状态",
    slotName: "status",
  },
  {
    title: "题目 id",
    dataIndex: "questionId",
  },
  {
    title: "提交者 id",
    dataIndex: "userId",
    slotName: "userId",
  },
  {
    title: "创建时间",
    slotName: "createTime",
  },
];
watchEffect(() => {
  loadData();
});
const onPageChange = (page: number) => {
  searchParams.value = {
    ...searchParams.value,
    current: page,
  };
};
//跳转到做题页面
const toQuestionPage = (question: Question) => {
  router.push({
    path: `/view/question/${question.id}`,
  });
};
const doSubmit = () => {
  //这里需要重置搜索页号
  searchParams.value = {
    ...searchParams.value,
    current: 1,
  };
};
</script>
<style scoped>
#questionSubmitView {
  max-width: 1480px;
  margin: 0 auto;
}
</style>
