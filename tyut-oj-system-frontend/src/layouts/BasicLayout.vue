<template>
  <div id="basicLayout">
    <a-layout style="min-height: 100vh">
      <!--头-->
      <a-layout-header class="header">
        <GlobalHeader></GlobalHeader>
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
<script lang="ts" setup>
import GlobalHeader from "@/components/GlobalHeader.vue";
</script>

<style scoped>
#basicLayout .header {
  margin-bottom: 16px;
  padding: 6px;
  box-shadow: 1px 1px 6px 1px rgba(0, 0, 0, 0.5) inset;
}

#basicLayout .content {
  background: linear-gradient(
    to bottom right,
    red,
    orange,
    yellow,
    green,
    blue,
    purple
  );
  margin-bottom: 16px;
}

#basicLayout .footer {
  margin-bottom: 16px;
  position: sticky;
  bottom: 0;
  right: 0;
  left: 0;
  text-align: center;
}
</style>
