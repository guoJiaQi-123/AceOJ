import { RouteRecordRaw } from "vue-router";
import AccessEnum from "@/access/accessEnum";
import UserLayout from "@/layouts/UserLayout.vue";
import UserLoginView from "@/views/user/UserLoginView.vue";
import UserRegisterView from "@/views/user/UserRegisterView.vue";
import QuestionView from "../views/question/QuestionView.vue";
import ViewQuestionView from "@/views/question/ViewQuestionView.vue";

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
      // 不需要在导航栏显示
      isHideView: true,
    },
  },
  {
    // 浏览题目页面，相当于首页，需要在导航栏显示
    path: "/",
    name: "首页",
    component: QuestionView,
  },
  {
    // 浏览题目页面，相当于首页，需要在导航栏显示
    path: "/question",
    name: "浏览题目",
    component: QuestionView,
  },
  {
    // 创建题目页面，需要显示在导航栏
    path: "/add/question",
    name: "创建题目",
    component: () => import("../views/question/AddQuestionView.vue"),
    meta: {
      access: AccessEnum.USER,
    },
  },
  {
    // 管理题目页面，需要显示在导航栏
    path: "/manage/question",
    name: "管理题目",
    component: () => import("../views/question/ManageQuestionView.vue"),
    meta: {
      access: AccessEnum.USER,
    },
  },
  {
    // 更新题目页面，不需要显示在导航栏
    path: "/update/question",
    name: "更新题目",
    component: () => import("../views/question/AddQuestionView.vue"),
    meta: {
      access: AccessEnum.ADMIN,
      isHideView: true,
    },
  },
  {
    path: "/view/question:id",
    name: "在线做题",
    props: true,
    component: ViewQuestionView,
    meta: {
      access: AccessEnum.USER,
      isHideView: true,
    },
  },
  {
    path: "/about",
    name: "关于我的",
    component: () => import("../views/AboutView.vue"),
  },
  // {
  //   path: "/admin",
  //   name: "管理员可见",
  //   component: () => import("../views/AdminView.vue"),
  //   meta: {
  //     access: accessEnum.ADMIN,
  //   },
  // },
  {
    path: "/noAuth",
    name: "没权限",
    component: () => import("../views/NoAuthView.vue"),
    meta: {
      isHideView: true,
    },
  },
];
