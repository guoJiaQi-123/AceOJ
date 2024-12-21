<template>
  <div id="addQuestionView"><h1 style="margin-left: 8%">创建题目</h1></div>
  <a-form :model="form" :style="{ width: '90%' }" @submit="handleSubmit">
    <a-form-item field="title" label="题目标题">
      <a-input v-model="form.title" placeholder="请输入标题"></a-input>
    </a-form-item>
    <a-form-item field="tags" label="标签">
      <a-input-tag v-model="form.tags" placeholder="请输入标签" allow-clear />
    </a-form-item>
    <a-form-item field="content" label="内容">
      <MdEditor
        :value="form.content"
        :handle-change="onContentChange"
        style="min-width: 100%"
      ></MdEditor>
    </a-form-item>
    <a-form-item field="answer" label="答案">
      <MdEditor
        :value="form.answer"
        :handle-change="onAnswerChange"
        style="min-width: 100%"
      ></MdEditor>
    </a-form-item>

    <a-form-item label="判题配置" :content-flex="false" :merge-props="false">
      <a-space direction="vertical" style="min-width: 480px">
        <a-form-item field="judgeConfig.timeLimit" label="时间限制">
          <a-input-number
            v-model="form.judgeConfig.timeLimit"
            placeholder="请输入时间消耗"
            mode="button"
            size="large"
            min="0"
          />
        </a-form-item>
        <a-form-item field="judgeConfig.memoryLimit" label="内存限制">
          <a-input-number
            v-model="form.judgeConfig.memoryLimit"
            placeholder="请输入内存限制"
            mode="button"
            size="large"
            min="0"
          />
        </a-form-item>
        <a-form-item field="judgeConfig.stackLimit" label="堆栈限制">
          <a-input-number
            v-model="form.judgeConfig.stackLimit"
            placeholder="请输入堆栈限制"
            mode="button"
            size="large"
            min="0"
          />
        </a-form-item>
      </a-space>
    </a-form-item>
    <a-form-item
      label="测试用例配置"
      :content-flex="false"
      :merge-props="false"
    >
      <a-form-item
        v-for="(judgeCaseItem, index) of form.judgeCase"
        :key="index"
        no-style
      >
        <a-space direction="vertical" style="min-width: 640px">
          <a-form-item
            :field="`form.judgeCase[${index}].input`"
            :label="`输入样例-${index + 1}`"
            :key="index"
          >
            <a-input
              v-model="judgeCaseItem.input"
              placeholder="请输入测试输入样例"
            />
          </a-form-item>
          <a-form-item
            :field="`form.judgeCase[${index}].output`"
            :label="`输出样例-${index + 1}`"
            :key="index"
          >
            <a-input
              v-model="judgeCaseItem.output"
              placeholder="请输入测试输出样例"
            />
          </a-form-item>
          <a-button @click="handleDelete(index)" status="danger">删除</a-button>
        </a-space>
      </a-form-item>
      <div style="margin-top: 32px">
        <a-button @click="handleAdd()" type="outline" status="success"
          >新增测试样例
        </a-button>
      </div>
    </a-form-item>
    <div style="margin-top: 16px"></div>
    <a-form-item>
      <a-button type="primary" style="min-width: 200px" @click="doSubmit()"
        >提交
      </a-button>
    </a-form-item>
  </a-form>
</template>
<script setup lang="ts">
import { onMounted, ref } from "vue";
import MdEditor from "@/components/MdEditor.vue";
import { QuestionControllerService } from "@/generated";
import { Message } from "@arco-design/web-vue";
import { useRoute } from "vue-router";
import router from "@/router";

const route = useRoute();
// 区分是添加页面还是更新页面
const updatePage = route.path.includes("update");
// 表单数据
let form = ref({
  answer: "",
  content: "",
  judgeCase: [
    {
      input: "",
      output: "",
    },
  ],
  judgeConfig: {
    memoryLimit: 1000,
    stackLimit: 1000,
    timeLimit: 1000,
  },
  tags: ["栈", "简单"],
  title: "A + B",
});

//根据题目id获取老的数据
const loadData = async () => {
  const id = route.query.id;
  if (!id) return;
  const res = await QuestionControllerService.getQuestionByIdUsingGet(
    id as any
  );
  if (res.code === 0) {
    form.value = res.data as any;
    if (!form.value.judgeCase) {
      form.value.judgeCase = [
        {
          input: "",
          output: "",
        },
      ];
    } else {
      form.value.judgeCase = JSON.parse(form.value.judgeCase as any);
    }
    if (!form.value.judgeConfig) {
      form.value.judgeConfig = {
        memoryLimit: 1000,
        stackLimit: 1000,
        timeLimit: 1000,
      };
    } else {
      form.value.judgeConfig = JSON.parse(form.value.judgeConfig as any);
    }
    if (!form.value.tags) {
      form.value.tags = [];
    } else {
      form.value.tags = JSON.parse(form.value.tags as any);
    }
  } else {
    Message.error("加载失败" + res.message);
  }
};
onMounted(() => {
  loadData();
});
const doSubmit = async () => {
  console.log(form.value);
  //判断更新还是新增
  if (updatePage) {
    // 更新
    const res = await QuestionControllerService.updateQuestionUsingPost(
      form.value
    );
    if (res.code === 0) {
      Message.success("更新成功");
      await router.push({
        path: "/manage/question",
      });
    } else {
      Message.error("更新失败" + res.message);
    }
  } else {
    // 添加
    const res = await QuestionControllerService.addQuestionUsingPost(
      form.value
    );
    if (res.code === 0) {
      Message.success("创建成功");
      await router.push({
        path: "/",
      });
    } else {
      Message.error("创建失败" + res.message);
    }
  }
};
const handleAdd = () => {
  form.value.judgeCase.push({
    input: "",
    output: "",
  });
};
const handleDelete = (index: number) => {
  form.value.judgeCase.splice(index, 1);
};
const handleSubmit = () => {
  console.log(1);
};
const onContentChange = (value: string) => {
  form.value.content = value;
};
const onAnswerChange = (value: string) => {
  form.value.answer = value;
};
</script>
<style scoped></style>
