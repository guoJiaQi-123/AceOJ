<template>
  <div id="app">
    <BasicLayout></BasicLayout>
  </div>
</template>
<script setup lang="ts">
import BasicLayout from "@/layouts/BasicLayout.vue";
import router from "@/router";
import store from "@/store";

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
</script>

<style scoped></style>
