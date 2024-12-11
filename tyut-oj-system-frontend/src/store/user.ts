import { StoreOptions } from "vuex";
// 用户状态管理模块
export default {
  namespaced: true,

  state: {
    loginUser: { userName: "未登录", role: "noLogin" },
  },

  getters: {},

  actions: {
    getLoginUser({ commit }, payload) {
      commit("updateUser", payload);
    },
  },

  mutations: {
    updateUser(state, payload) {
      state.loginUser = payload;
    },
  },
} as StoreOptions<any>;
