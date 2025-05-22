<template>
  <div class="login-view">
    <div class="row justify-content-center">
      <div class="col-md-6">
        <div class="card shadow">
          <div class="card-header bg-primary text-white">
            <h4 class="mb-0">Login</h4>
          </div>
          <div class="card-body">
            <div v-if="error" class="alert alert-danger" role="alert">
              {{ error }}
            </div>
            <form @submit.prevent="handleLogin">
              <div class="mb-3">
                <label for="email" class="form-label">Email</label>
                <input
                  type="email"
                  class="form-control"
                  id="email"
                  v-model="user.email"
                  required
                  placeholder="Seu email"
                />
              </div>
              <div class="mb-3">
                <label for="senha" class="form-label">Senha</label>
                <input
                  type="password"
                  class="form-control"
                  id="senha"
                  v-model="user.senha"
                  required
                  placeholder="Sua senha"
                />
              </div>
              <div class="d-grid gap-2">
                <button
                  type="submit"
                  class="btn btn-primary"
                  :disabled="loading"
                >
                  <span
                    v-if="loading"
                    class="spinner-border spinner-border-sm"
                    role="status"
                    aria-hidden="true"
                  ></span>
                  {{ loading ? "Entrando..." : "Entrar" }}
                </button>
              </div>
            </form>
            <div class="mt-3 text-center">
              <p>
                Não tem uma conta?
                <router-link to="/cadastro">Cadastre-se</router-link>
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "LoginView",
  data() {
    return {
      user: {
        email: "",
        senha: "",
      },
      loading: false,
      error: null,
    };
  },
  methods: {
    handleLogin() {
      this.loading = true;
      this.error = null;

      this.$store
        .dispatch("login", this.user)
        .then(() => {
          this.$router.push("/portal");
        })
        .catch((err) => {
          console.log(err);
          this.error = "Falha no login. Verifique suas credenciais.";
        })
        .finally(() => {
          this.loading = false;
        });
    },
  },
  created() {
    if (this.$store.getters.isLoggedIn) {
      this.$router.push("/portal");
    }
  },
};
</script>

<style scoped>
.login-view {
  padding-top: 50px;
}
</style>
