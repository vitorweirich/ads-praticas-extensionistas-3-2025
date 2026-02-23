<template>
  <div class="cadastro-view">
    <div class="row justify-content-center">
      <div class="col-md-8">
        <div class="card shadow">
          <div class="card-header bg-primary text-white">
            <h4 class="mb-0">Alterar senha</h4>
          </div>
          <div class="card-body">
            <div v-if="error" class="alert alert-danger" role="alert">
              {{ error }}
            </div>
            <div v-if="success" class="alert alert-success" role="alert">
              {{ success }}
            </div>
            <form @submit.prevent="handleResetPassword">
              <div class="row">
                <div class="col-md-6 mb-3">
                  <label for="senha" class="form-label">Nova senha</label>
                  <input
                    type="password"
                    class="form-control"
                    id="senha"
                    v-model="user.senha"
                    required
                    placeholder="Sua senha"
                  />
                  <small class="form-text text-muted"
                    >A nova senha deve ter pelo menos 6 caracteres</small
                  >
                </div>
                <div class="col-md-6 mb-3">
                  <label for="confirmarSenha" class="form-label"
                    >Confirmar nova Senha</label
                  >
                  <input
                    type="password"
                    class="form-control"
                    id="confirmarSenha"
                    v-model="confirmarSenha"
                    required
                    placeholder="Confirme sua nova senha"
                  />
                </div>
              </div>

              <div class="d-grid gap-2">
                <button
                  type="submit"
                  class="btn btn-primary"
                  :disabled="loading || !isFormValid"
                >
                  <span
                    v-if="loading"
                    class="spinner-border spinner-border-sm"
                    role="status"
                    aria-hidden="true"
                  ></span>
                  {{ loading ? "Resetando senha..." : "Resetar Senha" }}
                </button>
              </div>
            </form>
            <div class="mt-3 text-center">
              <p>
                Lembrou sua senha?
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
import { reactive, ref, computed, onMounted } from "vue";
import { useStore } from "vuex";
import { useRouter } from "vue-router";

const store = useStore();
const router = useRouter();

const user = reactive({
  nome: "",
  email: "",
  senha: "",
  telefone: "",
  cpfCnpj: "",
  endereco: "",
});
const confirmarSenha = ref("");
const loading = ref(false);
const error = ref(null);
const success = ref(null);

const isFormValid = computed(() => {
  return user.senha.length >= 6 && user.senha === confirmarSenha.value;
});

const handleResetPassword = async () => {
  if (!isFormValid.value) {
    error.value =
      "Por favor, preencha todos os campos obrigatorios corretamente.";
    return;
  }

  loading.value = true;
  error.value = null;
  success.value = null;

  try {
    await store.dispatch("resetPassword", {
      token: router.currentRoute.value.params.token,
      newPassword: user.senha,
    });
    success.value = "Senha atualizada com sucesso! Voce ja pode fazer login.";
    Object.assign(user, {
      nome: "",
      email: "",
      senha: "",
      telefone: "",
      cpfCnpj: "",
      endereco: "",
    });
    confirmarSenha.value = "";
    setTimeout(() => router.push("/login"), 2000);
  } catch (err) {
    console.error(err);
    error.value =
      err.response?.data?.message ||
      "Erro ao atualizar senha. Tente novamente.";
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  if (store.getters.isLoggedIn) router.push("/portal");
});
</script>

<style scoped>
.cadastro-view {
  padding-top: 30px;
  padding-bottom: 30px;
}
</style>
