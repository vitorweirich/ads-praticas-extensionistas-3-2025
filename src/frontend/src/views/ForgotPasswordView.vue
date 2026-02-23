<template>
  <div class="login-view">
    <div class="row justify-content-center">
      <div class="col-md-6">
        <div class="card shadow">
          <div class="card-header bg-primary text-white">
            <h4 class="mb-0">Informe seu email para</h4>
          </div>
          <div class="card-body">
            <div v-if="error" class="alert alert-danger" role="alert">
              {{ error }}
            </div>
            <!-- Success message section -->
            <div v-if="isRegistered" class="space-y-6 text-center">
              <div class="rounded-md p-4">
                <p class="text-base">
                  Se sua conta existir enviaremos um e-mail de confirmação para
                  <span class="font-semibold">{{ user.email }}</span
                  >.<br />
                  Por favor, verifique sua caixa de entrada e siga as instruções
                  para alterar sua senha.
                </p>
              </div>
            </div>
            <form v-if="!isRegistered" @submit.prevent="handleForgotPassword">
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
                  {{ loading ? "Carregando..." : "Recuperar Senha" }}
                </button>
              </div>
            </form>
            <div class="mt-3 text-center">
              <p>
                Já tem uma conta?
                <router-link to="/login">Faça login</router-link>
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
const isRegistered = ref(false);
const error = ref(null);

const handleForgotPassword = async () => {
  loading.value = true;
  error.value = null;
  try {
    await store.dispatch("forgotPassword", user.email);
    isRegistered.value = true;
  } catch (err) {
    console.error(err);
    error.value = "Falha ao enviar solicitação de recuperação de senha.";
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
