import { createStore } from "vuex";
import axios from "axios";
import router from "../router";

function isTokenExpired(token) {
  try {
    const base64Url = token.split(".")[1];
    const base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/");
    const payload = JSON.parse(window.atob(base64));

    if (!payload.exp) {
      return false;
    }

    const expirationDate = new Date(payload.exp * 1000);
    const currentDate = new Date();

    return currentDate >= expirationDate;
  } catch (e) {
    return true;
  }
}

axios.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");
    if (token) {
      if (isTokenExpired(token)) {
        localStorage.removeItem("token");
        localStorage.removeItem("user");
        delete axios.defaults.headers.common["Authorization"];

        router.push("/login");
      } else {
        config.headers["Authorization"] = "Bearer " + token;
      }
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  },
);

axios.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    if (error?.response && error?.response?.status === 401) {
      localStorage.removeItem("token");
      localStorage.removeItem("user");
      delete axios.defaults.headers.common["Authorization"];

      router.push("/login");
    }
    return Promise.reject(error);
  },
);

const store = createStore({
  state: {
    status: "",
    token: localStorage.getItem("token") || "",
    user: JSON.parse(localStorage.getItem("user")) || {},
    campanhas: { campanhas: [], isLoading: true },
    transparencia: [],
  },
  getters: {
    isLoggedIn: (state) => !!state.token,
    authStatus: (state) => state.status,
    currentUser: (state) => state.user,
    isAdmin: (state) => {
      return (
        state.user &&
        state.user.roles &&
        state.user.roles.includes("ROLE_ADMINISTRADOR")
      );
    },
    campanhasAtivas: (state) => {
      return state.campanhas.filter((campanha) => campanha.status === "ATIVA");
    },
  },
  mutations: {
    auth_request(state) {
      state.status = "loading";
    },
    auth_success(state, { token, user }) {
      state.status = "success";
      state.token = token;
      state.user = user;
    },
    auth_error(state) {
      state.status = "error";
    },
    logout(state) {
      state.status = "";
      state.token = "";
      state.user = {};
    },
    set_campanhas(state, campanhas) {
      state.campanhas = { campanhas, isLoading: false };
    },
    set_transparencia(state, transparencia) {
      state.transparencia = transparencia;
    },
  },
  actions: {
    checkToken({ commit }) {
      const token = localStorage.getItem("token");
      if (token && isTokenExpired(token)) {
        commit("logout");
        localStorage.removeItem("token");
        localStorage.removeItem("user");
        delete axios.defaults.headers.common["Authorization"];
      }
    },

    login({ commit }, user) {
      return new Promise((resolve, reject) => {
        commit("auth_request");
        axios
          .post(`${process.env.VUE_APP_API_BASE_URL}/api/auth/login`, user)
          .then((resp) => {
            const token = resp.data.accessToken;
            const user = {
              id: resp.data.id,
              nome: resp.data.nome,
              email: resp.data.email,
              roles: resp.data.roles,
            };
            localStorage.setItem("token", token);
            localStorage.setItem("user", JSON.stringify(user));
            axios.defaults.headers.common["Authorization"] = "Bearer " + token;
            commit("auth_success", { token, user });
            resolve(resp);
          })
          .catch((err) => {
            commit("auth_error");
            localStorage.removeItem("token");
            localStorage.removeItem("user");
            reject(err);
          });
      });
    },
    register({ commit }, user) {
      return new Promise((resolve, reject) => {
        commit("auth_request");
        axios
          .post(`${process.env.VUE_APP_API_BASE_URL}/api/auth/cadastro`, user)
          .then((resp) => {
            resolve(resp);
          })
          .catch((err) => {
            commit("auth_error");
            reject(err);
          });
      });
    },
    logout({ commit }) {
      return new Promise((resolve) => {
        commit("logout");
        localStorage.removeItem("token");
        localStorage.removeItem("user");
        delete axios.defaults.headers.common["Authorization"];
        resolve();
      });
    },

    sessionExchange({ commit }, { transferToken }) {
      return new Promise((resolve, reject) => {
        commit("auth_request");

        axios
          .post(
            `${process.env.VUE_APP_API_BASE_URL}/api/auth/session-exchange`,
            { token: transferToken },
          )
          .then((resp) => {
            const token = resp.data.accessToken;

            const user = {
              id: resp.data.id,
              nome: resp.data.nome,
              email: resp.data.email,
              roles: resp.data.roles,
            };

            localStorage.removeItem("token");
            localStorage.removeItem("user");

            localStorage.setItem("token", token);
            localStorage.setItem("user", JSON.stringify(user));

            axios.defaults.headers.common["Authorization"] = "Bearer " + token;

            commit("auth_success", { token, user });

            resolve(resp);
          })
          .catch((err) => {
            commit("auth_error");
            reject(err);
          });
      });
    },

    forgotPassword(_, email) {
      return new Promise((resolve, reject) => {
        axios
          .post(`${process.env.VUE_APP_API_BASE_URL}/api/auth/esqueci-senha`, {
            email,
          })
          .then((resp) => {
            resolve(resp);
          })
          .catch((err) => {
            reject(err);
          });
      });
    },
    resetPassword(_, body) {
      return new Promise((resolve, reject) => {
        axios
          .post(
            `${process.env.VUE_APP_API_BASE_URL}/api/auth/resetar-senha`,
            body,
          )
          .then((resp) => {
            resolve(resp);
          })
          .catch((err) => {
            reject(err);
          });
      });
    },
    fetchCampanhas({ commit }) {
      return new Promise((resolve, reject) => {
        axios
          .get(`${process.env.VUE_APP_API_BASE_URL}/api/campanhas/publicas`)
          .then((resp) => {
            commit("set_campanhas", resp.data);
            resolve(resp);
          })
          .catch((err) => {
            reject(err);
          });
      });
    },
    fetchTransparencia({ commit }) {
      return new Promise((resolve, reject) => {
        axios
          .get(`${process.env.VUE_APP_API_BASE_URL}/api/transparencia/publica`)
          .then((resp) => {
            commit("set_transparencia", resp.data);
            resolve(resp);
          })
          .catch((err) => {
            reject(err);
          });
      });
    },
  },
  modules: {},
});

export default store;
