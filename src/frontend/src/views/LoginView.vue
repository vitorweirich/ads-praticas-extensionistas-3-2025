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
              <p>
                Esqueci minha senha.
                <router-link to="/esqueci-senha">Recuperar senha</router-link>
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from "vue";
import { useStore } from "vuex";
import { useRouter } from "vue-router";

const store = useStore();
const router = useRouter();

const user = reactive({ email: "", senha: "" });
const loading = ref(false);
const error = ref(null);

const handleLogin = async () => {
  loading.value = true;
  error.value = null;
  try {
    await store.dispatch("login", user);
    router.push("/portal");
  } catch (err) {
    console.error(err);
    error.value = "Falha no login. Verifique suas credenciais.";
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  if (store.getters.isLoggedIn) router.push("/portal");
});
</script>

<style scoped>
.login-view {
  padding-top: 50px;
}
</style>
