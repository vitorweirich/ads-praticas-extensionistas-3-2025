import { createRouter, createWebHistory } from "vue-router";
import HomeView from "../views/HomeView.vue";
import LoginView from "../views/LoginView.vue";
import CadastroView from "../views/CadastroView.vue";
import PortalView from "../views/PortalView.vue";
import TransparenciaView from "../views/TransparenciaView.vue";
import PerfilView from "../views/PerfilView.vue";
import AdminView from "../views/AdminView.vue";
import NotFoundView from "../views/NotFoundView.vue";
import CampanhaDetalheView from "../views/CampanhaDetalheView.vue";
import AdministracaoCampanhaView from "../views/AdministracaoCampanhaView.vue";
import ContatoView from "../views/ContatoView.vue";

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
    path: "/campanha/:id",
    name: "campanha-detalhe",
    component: CampanhaDetalheView,
  },
  {
    path: "/transparencia",
    name: "transparencia",
    component: TransparenciaView,
  },
  {
    path: "/contato",
    name: "contato",
    component: ContatoView,
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
  {
    path: "/admin/campanhas",
    name: "admin-campanhas",
    component: AdministracaoCampanhaView,
    meta: { requiresAuth: true, requiresAdmin: true },
  },
  {
    path: "/404",
    name: "notFound",
    component: NotFoundView,
  },
  {
    path: "/:pathMatch(.*)*",
    redirect: "/404",
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

router.beforeEach((to, from, next) => {
  const loggedIn = localStorage.getItem("user");

  if (to.matched.some((record) => record.meta.requiresAuth)) {
    if (!loggedIn) {
      next({ path: "/login" });
      return;
    }

    if (to.matched.some((record) => record.meta.requiresAdmin)) {
      const user = JSON.parse(loggedIn);
      if (!user.roles.includes("ROLE_ADMINISTRADOR")) {
        next({ path: "/portal" });
        return;
      }
    }
  }

  next();
});

export default router;
