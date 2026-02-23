<template>
  <div class="contato-view container py-4">
    <h1>Contato</h1>

    <div class="row">
      <div class="col-md-6">
        <div class="card mb-3">
          <div class="card-body">
            <h5 class="card-title">Enviar mensagem</h5>
            <form @submit.prevent="enviar">
              <div class="mb-3">
                <div class="d-flex flex-row">
                  <label class="form-label mb-0">Seu email</label>
                  <span
                    class="ms-2 text-muted"
                    role="button"
                    tabindex="0"
                    data-bs-toggle="tooltip"
                    data-bs-placement="top"
                    title="Informe seu email verdadeiro para que possamos entrar em contato se precisarmos de mais informações"
                    aria-label="Ajuda: informar seu email"
                  >
                    <i class="fas fa-question-circle"></i>
                  </span>
                </div>
                <input
                  v-model="form.email"
                  type="email"
                  class="form-control"
                  required
                  data-bs-toggle="tooltip"
                  data-bs-placement="top"
                  title="Informe seu email verdadeiro para que possamos entrar em contato se precisarmos de mais informações"
                />
              </div>
              <div class="mb-3">
                <label class="form-label">Título</label>
                <input v-model="form.titulo" class="form-control" required />
              </div>
              <div class="mb-3">
                <label class="form-label">Descrição</label>
                <textarea
                  v-model="form.descricao"
                  class="form-control"
                  rows="5"
                  required
                ></textarea>
              </div>
              <button class="btn btn-primary" :disabled="enviando">
                <span
                  v-if="enviando"
                  class="spinner-border spinner-border-sm me-2"
                ></span>
                Enviar
              </button>
            </form>
          </div>
        </div>
      </div>

      <div class="col-md-6">
        <div class="card mb-3">
          <div class="card-body">
            <h5 class="card-title">
              Ou envie um e-mail para um dos nossos contatos
            </h5>
            <ul class="list-unstyled">
              <li v-for="email in emails" :key="email">
                <i class="fas fa-envelope me-2"></i>{{ email }}
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useStore } from "vuex";
import axios from "axios";
import router from "../router";

const form = ref({ email: "", titulo: "", descricao: "" });
const emails = ref([]);
const enviando = ref(false);
const store = useStore();

const carregarEmails = async () => {
  try {
    const resp = await axios.get(
      `${process.env.VUE_APP_API_BASE_URL}/api/contato/emails`
    );
    emails.value = resp.data;
  } catch (e) {
    console.error("Erro ao carregar emails", e);
    emails.value = ["suporte@doacoes.local"];
  }
};

const enviar = async () => {
  enviando.value = true;
  try {
    await axios.post(
      `${process.env.VUE_APP_API_BASE_URL}/api/contato/enviar`,
      form.value
    );
    alert("Mensagem enviada com sucesso");
    form.value = { email: "", titulo: "", descricao: "" };

    router.push({ name: "home" });
  } catch (e) {
    console.error(e);
    alert("Erro ao enviar mensagem");
  } finally {
    enviando.value = false;
  }
};

onMounted(() => {
  carregarEmails();

  try {
    if (store.getters && store.getters.isLoggedIn) {
      const user = store.getters.currentUser || {};
      if (user.email) {
        form.value.email = user.email;
      }
    }
  } catch (e) {
    console.error("Erro ao inicializar email do usuário autenticado", e);
  }
});
</script>

<style scoped>
.contato-view h1 {
  margin-bottom: 20px;
}
</style>
