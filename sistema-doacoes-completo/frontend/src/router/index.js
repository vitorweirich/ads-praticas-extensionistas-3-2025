import Vue from "vue";
import VueRouter from "vue-router";
import HomeView from "../views/HomeView.vue";
import LoginView from "../views/LoginView.vue";
import CadastroView from "../views/CadastroView.vue";
import PortalView from "../views/PortalView.vue";
import TransparenciaView from "../views/TransparenciaView.vue";
import PerfilView from "../views/PerfilView.vue";
import AdminView from "../views/AdminView.vue";

Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    name: "home",
    component: HomeView,
  },
  {
    path: "/login",
    name: "login",
    component: LoginView,
  },
  {
    path: "/cadastro",
    name: "cadastro",
    component: CadastroView,
  },
  {
    path: "/portal",
    name: "portal",
    component: PortalView,
  },
  {
    path: "/transparencia",
    name: "transparencia",
    component: TransparenciaView,
  },
  {
    path: "/perfil",
    name: "perfil",
    component: PerfilView,
    meta: { requiresAuth: true },
  },
  {
    path: "/admin",
    name: "admin",
    component: AdminView,
    meta: { requiresAuth: true, requiresAdmin: true },
  },
];

const router = new VueRouter({
  mode: "hash",
  base: process.env.BASE_URL,
  routes,
});

// TODO: Revisar
router.beforeEach((to, from, next) => {
  const loggedIn = localStorage.getItem("user");

  if (to.matched.some((record) => record.meta.requiresAuth)) {
    if (!loggedIn) {
      next("/login");
      return;
    }

    if (to.matched.some((record) => record.meta.requiresAdmin)) {
      const user = JSON.parse(loggedIn);
      if (!user.roles.includes("ROLE_ADMINISTRADOR")) {
        next("/portal");
        return;
      }
    }
  }

  next();
});

export default router;
