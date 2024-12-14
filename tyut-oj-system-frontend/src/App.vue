<template>
  <div id="app">
    <template v-if="route.path.startsWith('/user')">
      <router-view></router-view>
    </template>
    <template v-else>
      <BasicLayout></BasicLayout>
    </template>
  </div>
</template>
<script lang="ts" setup>
import BasicLayout from "@/layouts/BasicLayout.vue";
import { onMounted } from "vue";
import axios from "axios";
import { useRoute } from "vue-router";

const route = useRoute();
const doInit = () => {
  console.log("hello 欢迎来到我的项目");
};

onMounted(() => {
  doInit();
});

// axios全局配置
axios.interceptors.request.use(
  function (config) {
    return config;
  },
  function (error) {
    return Promise.reject(error);
  }
);

axios.interceptors.response.use(
  function (response) {
    return response;
  },
  function (error) {
    return Promise.reject(error);
  }
);
</script>

<style scoped></style>
