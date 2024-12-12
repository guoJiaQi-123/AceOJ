// 权限管理
import router from "@/router";
import store from "@/store";
import CheckAccess from "@/access/checkAccess";
import AccessEnum from "@/access/accessEnum";

router.beforeEach(async (to, from, next) => {
  // 登录用户
  const loginUser = store.state.user.loginUser;
  // 如果没登录过，则自动登录
  // if (!loginUser || !loginUser.userRole) {
  //   await store.dispatch("user/getLoginUser");
  //   return;
  // }
  // 访问的页面需要的权限
  const needAccess = to.meta?.access ?? AccessEnum.NOT_LOGIN;
  // 如果访问的页面需要登录
  if (needAccess !== AccessEnum.NOT_LOGIN) {
    // 如果没登录，跳转到登录页面
    if (!loginUser || !loginUser.userRole) {
      next(`/user/login?redirect=${to.fullPath}`);
      return;
    }
    if (!CheckAccess(loginUser, needAccess)) {
      next("/noAuth");
      return;
    }
  }
  next();
});
