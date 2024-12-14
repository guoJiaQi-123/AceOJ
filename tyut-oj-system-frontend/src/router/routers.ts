import { RouteRecordRaw } from "vue-router";
import HomeView from "@/views/HomeView.vue";
import accessEnum from "@/access/accessEnum";
import UserLayout from "@/layouts/UserLayout.vue";
import UserLoginView from "@/views/UserLoginView.vue";
import UserRegisterView from "@/views/UserRegisterView.vue";

export const routes: Array<RouteRecordRaw> = [
  {
    path: "/user",
    name: "用户页面",
    component: UserLayout,
    children: [
      {
        path: "/user/login",
        name: "用户登陆",
        component: UserLoginView,
      },
      {
        path: "/user/register",
        name: "用户注册",
        component: UserRegisterView,
      },
    ],
    meta: {
      isHideView: true,
    },
  },
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
