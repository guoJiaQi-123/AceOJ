<template>
  <a-row id="globalHeader" :wrap="false" align="center">
    <a-col flex="auto">
      <a-menu
        :selected-keys="selectedKeys"
        mode="horizontal"
        @menu-item-click="doMenuClick"
      >
        <a-menu-item
          key="0"
          :style="{ padding: 0, marginRight: '38px' }"
          disabled
        >
          <div @click="logo()">
            <div class="title-bar">
              <img alt="" class="logo" src="@/assets/logo.png" />
              <div class="title">AceCoding 代码练习平台</div>
            </div>
          </div>
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

<script lang="ts" setup>
import { useRouter } from "vue-router";
import { computed, ref } from "vue";
import { routes } from "@/router/routers";
import { useStore } from "vuex";
import checkAccess from "@/access/checkAccess";

const store = useStore();
const router = useRouter();

const logo = () => {
  router.push({
    path: "/",
  });
};
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
// setTimeout(() => {
//   store.dispatch("user/getLoginUser", {
//     userName: "LAL",
//     userRole: accessEnum.ADMIN,
//   });
// }, 3000);

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
