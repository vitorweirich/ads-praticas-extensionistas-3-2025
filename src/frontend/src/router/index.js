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
import ForgotPasswordView from "../views/ForgotPasswordView.vue";
import ResetarSenhaView from "../views/ResetarSenhaView.vue";
import SessionTransferView from "../views/SessionTransferView.vue";

const routes = [
  {
    path: "/",
    name: "home",
    component: HomeView,
    alias: "/app",
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
    path: "/esqueci-senha",
    name: "esqueci-senha",
    component: ForgotPasswordView,
  },
  {
    path: "/resetar-senha/:token",
    name: "resetar-senha",
    component: ResetarSenhaView,
  },
  {
    path: "/session-transfer/:token",
    name: "SessionTransfer",
    component: SessionTransferView,
  },
  {
    path: "/portal",
    name: "portal",
    component: PortalView,
    alias: "/app/portal",
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
    name: "notResolved",
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

router.beforeEach((to, from, next) => {
  const loggedIn = localStorage.getItem("user");

  if (to.meta.requiresAuth && !loggedIn) {
    return next("/login");
  }

  if (to.meta.requiresAdmin && loggedIn) {
    const user = JSON.parse(loggedIn);
    if (!user.roles.includes("ROLE_ADMINISTRADOR")) {
      return next("/portal");
    }
  }

  const appPath = "/app" + to.fullPath;

  if (!loggedIn && !to.path.startsWith("/app")) {
    const resolved = router.resolve(appPath);

    if (resolved.name && resolved.name !== "notResolved") {
      return next(appPath);
    }
  }

  next();
});

export default router;
