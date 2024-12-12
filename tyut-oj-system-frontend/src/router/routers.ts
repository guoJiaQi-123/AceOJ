import { RouteRecordRaw } from "vue-router";
import HomeView from "@/views/HomeView.vue";
import accessEnum from "@/access/accessEnum";

export const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    name: "浏览题目",
    component: HomeView,
  },
  {
    path: "/about",
    name: "关于我的",
    component: () => import("../views/AboutView.vue"),
  },
  {
    path: "/admin",
    name: "管理员可见",
    component: () => import("../views/AdminView.vue"),
    meta: {
      access: accessEnum.ADMIN,
    },
  },
  {
    path: "/noAuth",
    name: "没权限",
    component: () => import("../views/NoAuthView.vue"),
    meta: {
      isHideView: true,
    },
  },
];
