<template>
  <div id="app">
    <BasicLayout></BasicLayout>
  </div>
</template>
<script lang="ts" setup>
import BasicLayout from "@/layouts/BasicLayout.vue";
import router from "@/router";
import store from "@/store";
import { onMounted } from "vue";
import axios from "axios";

const doInit = () => {
  console.log("hello 欢迎来到我的项目");
};

onMounted(() => {
  doInit();
});
// 权限管理
router.beforeEach((to, from, next) => {
  // 如果想要访问管理员才能访问的页面
  if (to.meta.access === "canAdmin") {
    // 判断当前用户是否有管理员权限
    if (store.state.user.loginUser.role !== "admin") {
      next("/noAuth");
    }
  }
  next();
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
