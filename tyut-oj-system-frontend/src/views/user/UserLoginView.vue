<template>
  <div id="UserLoginView">
    <h1 style="margin-bottom: 50px; margin-top: 10%">用户登录</h1>
    <a-form
      style="max-width: 33%; margin: 0 auto"
      :model="form"
      @submit="handleSubmit"
      label-align="left"
      auto-label-width
    >
      <a-form-item field="userAccount" label="账号">
        <a-input v-model="form.userAccount" placeholder="请输入账号" />
      </a-form-item>
      <a-form-item field="userPassword" label="密码" tooltip="密码不少于8位">
        <a-input-password
          v-model="form.userPassword"
          placeholder="请输入密码"
        />
      </a-form-item>
      <a-form-item>
        <a-button html-type="submit" style="width: 240px" type="primary"
          >登录
        </a-button>
        <a-button @click="register" style="width: 240px" id="register"
          >注册
        </a-button>
      </a-form-item>
    </a-form>
  </div>
</template>
<script setup lang="ts">
import { reactive } from "vue";
import { Message } from "@arco-design/web-vue";
import { useRouter } from "vue-router";
import { useStore } from "vuex";
import { UserControllerService, UserLoginRequest } from "@/generated";

const router = useRouter();
const store = useStore();

const form = reactive({
  userAccount: "",
  userPassword: "",
} as UserLoginRequest);

const register = () => {
  router.push({
    path: "/user/register",
  });
};

const handleSubmit = async () => {
  const res = await UserControllerService.userLoginUsingPost(form);
  //登录成功，跳转到主页
  if (res.code === 0) {
    await store.dispatch("user/getLoginUser"); // 状态管理
    Message.success("登录成功");
    await router.push({
      path: "/",
      replace: true,
    });
  } else {
    Message.error("登录失败" + res.message);
  }
};
</script>
<style>
#register {
  margin-left: 10%;
}
</style>
