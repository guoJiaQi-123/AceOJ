<template>
  <a-row id="globalHeader" align="center" :wrap="false">
    <a-col flex="auto">
      <a-menu
        mode="horizontal"
        :selected-keys="selectedKeys"
        @menu-item-click="doMenuClick"
      >
        <a-menu-item
          key="0"
          :style="{ padding: 0, marginRight: '38px' }"
          disabled
        >
          <a href="https://www.tyut.edu.cn/" target="_blank">
            <div class="title-bar">
              <img src="@/assets/logo.png" alt="" class="logo" />
              <div class="title">TYUT - OJ在线判题平台</div>
            </div>
          </a>
        </a-menu-item>
        <a-menu-item v-for="item in visibleRouter" :key="item.path">
          {{ item.name }}
        </a-menu-item>
      </a-menu>
    </a-col>
    <a-col flex="100px">
      <div>{{ store.state.user.loginUser.userName }}</div>
    </a-col>
  </a-row>
</template>

<script setup lang="ts">
import { useRouter } from "vue-router";
import { computed, ref } from "vue";
import { routes } from "@/router/routers";
import { useStore } from "vuex";
import checkAccess from "@/access/checkAccess";
import accessEnum from "@/access/accessEnum";

const store = useStore();
const router = useRouter();

// 需要展示的路由
const visibleRouter = computed(() => {
  const loginUser = store.state.user.loginUser;
  console.log(loginUser);
  return routes.filter((value, index) => {
    if (value.meta?.isHideView) {
      return false;
    }
    // 根据权限过滤菜单
    if (!checkAccess(loginUser, value?.meta?.access as string)) {
      return false;
    }
    return true;
  });
});
setTimeout(() => {
  store.dispatch("user/getLoginUser", {
    userName: "LAL",
    userRole: accessEnum.ADMIN,
  });
}, 3000);

//默认主页
const selectedKeys = ref(["/"]);
//路由跳转后，更新激活项，高亮显示当前页路由
router.afterEach((to) => {
  selectedKeys.value = [to.path];
});

// 点击路由跳转
const doMenuClick = (key: string) => {
  router.push({
    path: key,
  });
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.title-bar {
  display: flex;
  align-items: center;
}

.title {
  color: #444;
  margin-left: 16px;
  font-weight: bold;
}

.logo {
  height: 48px;
}
</style>
