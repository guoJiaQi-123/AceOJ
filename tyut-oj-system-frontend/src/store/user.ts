import { StoreOptions } from "vuex";

import ACCESS_ENUM from "@/access/accessEnum";
import { UserControllerService } from "@/generated";
// 用户状态管理模块
export default {
  namespaced: true,

  state: () => ({
    loginUser: {
      userName: "未登录",
    },
  }),

  getters: {},

  actions: {
    async getLoginUser({ commit, state }) {
      const res = await UserControllerService.getLoginUserUsingGet();
      if (res.code === 0) {
        commit("updateUser", res.data);
      } else {
        commit("updateUser", {
          ...state.loginUser,
          userRole: ACCESS_ENUM.NOT_LOGIN,
        });
      }
    },
  },

  mutations: {
    updateUser(state, payload) {
      state.loginUser = payload;
    },
  },
} as StoreOptions<any>;
