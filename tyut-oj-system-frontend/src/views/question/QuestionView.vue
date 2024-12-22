<template>
  <div id="questionView">
    <h1>浏览题目</h1>
    <a-form :model="searchParams" layout="inline">
      <a-form-item field="title" label="名称" style="min-width: 240px">
        <a-input v-model="searchParams.title" placeholder="请输入名称" />
      </a-form-item>
      <a-form-item field="tags" label="标签" style="min-width: 240px">
        <a-input-tag v-model="searchParams.tags" placeholder="请输入标签" />
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
      <template #acceptedNum="{ record }">
        <a-space wrap>
          {{
            `${
              record.submitNum ? record.acceptedNum / record.acceptedNum : "0"
            }% (${record.acceptedNum}/${record.submitNum})`
          }}
        </a-space>
      </template>
      <template #createTime="{ record }">
        <a-space wrap> {{ moment(record).format("YYYY-MM-DD") }}</a-space>
      </template>
      <template #optional="{ record }">
        <a-space>
          <a-button type="primary" @click="doQuestion(record)">做题</a-button>
        </a-space>
      </template>
    </a-table>
  </div>
</template>
<script setup lang="ts">
import { onMounted, ref, watchEffect } from "vue";
import { Question, QuestionControllerService } from "@/generated";
import { Message } from "@arco-design/web-vue";
import { useRouter } from "vue-router";
import moment from "moment";

const total = ref(0);
const router = useRouter();
const dataList = ref([]);
const searchParams = ref({
  title: "",
  tags: [],
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
    title: "题目标题",
    dataIndex: "title",
  },

  {
    title: "题目标签",
    slotName: "tags",
  },

  {
    title: "通过率",
    slotName: "acceptedNum",
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

const pageChange = (page: number) => {
  searchParams.value = {
    ...searchParams.value,
    current: page,
  };
};
const doQuestion = (question: Question) => {
  router.push({
    path: `/view/question${question.id}`,
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
#questionView {
  max-width: 1480px;
  margin: 0 auto;
}
</style>
