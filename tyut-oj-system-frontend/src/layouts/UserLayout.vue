<template>
  <div id="userLayout">
    <a-layout style="min-height: 100vh">
      <!--头-->
      <a-layout-header class="header">
        <a-space>
          <img
            style="color: white"
            alt=""
            class="logo"
            src="@/assets/logo.png"
          />
          <h1 class="title">TYUT - OJ在线判题平台</h1>
        </a-space>
      </a-layout-header>
      <!--动态页面-->
      <a-layout-content class="content">
        <router-view />
      </a-layout-content>
      <!--页脚-->
      <a-layout-footer class="footer"> Tyut —— OJ在线判题系统</a-layout-footer>
    </a-layout>
  </div>
</template>
<script lang="ts" setup></script>

<style scoped>
#userLayout {
  text-align: center;
  background: url(https://gw.alipayobjects.com/zos/rmsportal/FfdJeJRQWjEeGTpqgBKj.png)
    0% 0% / 100% 100%;
}

#userLayout .header {
  margin-top: 16px;
  padding: 6px;
  text-align: center;
}

#userLayout .content {
  margin-bottom: 16px;
}

#userLayout .footer {
  margin-bottom: 16px;
  position: sticky;
  bottom: 0;
  right: 0;
  left: 0;
  text-align: center;
}

.title {
  color: #444;
  margin-left: 16px;
  font-weight: bold;
}

.logo {
  height: 56px;
}
</style>
