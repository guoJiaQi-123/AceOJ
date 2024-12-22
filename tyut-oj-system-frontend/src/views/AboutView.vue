<template>
  <div class="about">
    <a-form :model="userForm" layout="inline">
      <a-form-item field="title" label="用户名" style="min-width: 240px">
        <a-input v-model="userForm.userName" placeholder="请输入用户名" />
      </a-form-item>

      <a-form-item>
        <a-button @click="doSubmit()" type="primary">提交</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>
<script setup lang="ts">
import { ref } from "vue";
import { UserControllerService, UserUpdateMyRequest } from "@/generated";
import { Message } from "@arco-design/web-vue";
import store from "@/store";

const userForm = ref<UserUpdateMyRequest>({});
const doSubmit = async () => {
  console.log(userForm);
  const res = await UserControllerService.updateMyUserUsingPost(userForm.value);
  if (res.code === 0) {
    Message.success("个人信息更新成功");
    await store.dispatch("user/getLoginUser");
  } else {
    Message.error("个人信息更新失败，" + res.message);
  }
};
</script>
<style scoped>
#aboutView {
}
</style>
