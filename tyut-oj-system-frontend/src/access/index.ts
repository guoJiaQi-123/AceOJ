// 权限管理
import router from "@/router";
import store from "@/store";
import CheckAccess from "@/access/checkAccess";
import AccessEnum from "@/access/accessEnum";

router.beforeEach(async (to, from, next) => {
  /*
      核心逻辑：
        如果当前用户没登录过，则自动调用后端登录接口获取用户登录信息，（可能返回登录成功或者没有登录）
        然后根据我们想要跳转的页面，判断这个页面需要什么权限，如果需要登录权限而当前用户为未登录，则跳转到登录页面
        如果是权限不够，则跳转到无权限页面
   */
  // 登录用户
  let loginUser = store.state.user.loginUser;
  // 如果没登录过，则自动登录
  if (!loginUser || !loginUser.userRole) {
    await store.dispatch("user/getLoginUser");
    loginUser = store.state.user.loginUser;
  }
  // 访问的页面需要的权限
  const needAccess = (to.meta?.access as string) ?? AccessEnum.NOT_LOGIN;
  // 如果访问的页面需要登录
  if (needAccess !== AccessEnum.NOT_LOGIN) {
    // 如果没登录，跳转到登录页面
    if (
      !loginUser ||
      !loginUser.userRole ||
      loginUser.userRole === AccessEnum.NOT_LOGIN
    ) {
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
