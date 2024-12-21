<template>
  <div id="UserLoginView">
    <h1 style="margin-bottom: 50px; margin-top: 10%">用户注册</h1>
    <a-form
      :model="form"
      auto-label-width
      label-align="left"
      style="max-width: 33%; margin: 0 auto"
      @submit="handleSubmit"
    >
      <a-form-item field="账号" label="账号">
        <a-input v-model="form.userAccount" placeholder="请输入账号" />
      </a-form-item>
      <a-form-item field="密码" label="密码" tooltip="密码不少于8位">
        <a-input-password
          v-model="form.userPassword"
          placeholder="请输入密码"
        />
      </a-form-item>
      <a-form-item field="确认密码" label="确认密码" required>
        <a-input-password
          v-model="form.checkPassword"
          placeholder="请输入密码"
        />
      </a-form-item>
      <a-form-item>
        <a-button html-type="submit" style="width: 240px" type="primary"
          >注册
        </a-button>
        <a-button id="register" style="width: 240px" @click="toLoginView"
          >返回登录页
        </a-button>
      </a-form-item>
    </a-form>
  </div>
</template>
<script lang="ts" setup>
import { reactive } from "vue";
import { Message } from "@arco-design/web-vue";
import { useRouter } from "vue-router";
import { UserControllerService, UserRegisterRequest } from "@/generated";

const router = useRouter();

const form = reactive({
  userAccount: "",
  userPassword: "",
  checkPassword: "",
} as UserRegisterRequest);

const toLoginView = () => {
  router.push({
    path: "/user/login",
  });
};

const handleSubmit = async () => {
  const res = await UserControllerService.userRegisterUsingPost(form);
  //注册成功，跳转到主页
  if (res.code === 0) {
    Message.success("注册成功");
    await router.push({
      path: "/user/login",
    });
  } else {
    Message.error("注册失败，" + res.message);
  }
};
</script>
<style>
#register {
  margin-left: 10%;
}
</style>
