<template>
  <div
    id="code-editor"
    ref="codeEditorRef"
    style="min-height: 400px; height: 70vh"
  ></div>
</template>
<script lang="ts" setup>
import * as monaco from "monaco-editor";
import {
  defineProps,
  onMounted,
  ref,
  toRaw,
  watchEffect,
  withDefaults,
} from "vue";

// 外部组件传值
interface Props {
  value: string;
  language: string;
  handleChange: (v: string) => void;
}

// 给默认值
const props = withDefaults(defineProps<Props>(), {
  value: () => "",
  language: () => "java",
  handleChange: (v: string) => {
    console.log(v);
  },
});

const codeEditorRef = ref();
const codeEditor = ref();
onMounted(() => {
  if (!codeEditorRef.value) {
    return;
  }
  // 创建一个 Monaco 编辑器实例，并将其赋值给 codeEditorRef.value
  codeEditor.value = monaco.editor.create(codeEditorRef.value, {
    // 设置编辑器的初始内容，来自 props.value
    value: props.value,
    // 设置编辑器的语言为 Java
    language: props.language,
    // 启用自动布局，使编辑器能够自动调整大小以适应容器
    automaticLayout: true,
    // 启用小地图，用于提供代码的概览
    minimap: {
      enabled: true,
      scale: 20,
      sectionHeaderFontSize: 20,
      maxColumn: 100,
    },
    // 显示行号
    lineNumbers: "on",
    // 启用圆形选区，使选区显示为圆形样式
    roundedSelection: true,
    // 允许滚动超过最后一行，方便用户滚动到空白区域
    scrollBeyondLastLine: true,
    // 编辑器是否为只读模式，这里设置为 false，表示可编辑
    readOnly: false,
    // 设置编辑器的主题为 vs-dark
    theme: "vs-dark",
  });
  //编辑监听内容变化
  codeEditor.value.onDidChangeModelContent(() => {
    props.handleChange(toRaw(codeEditor.value).getValue());
  });
});
</script>

<style scoped></style>
