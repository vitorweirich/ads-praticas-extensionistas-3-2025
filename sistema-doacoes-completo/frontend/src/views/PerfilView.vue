<template>
  <div class="perfil-view">
    <div class="container py-5">
      <div class="row">
        <!-- Sidebar / Menu lateral -->
        <div class="col-lg-3 mb-4">
          <div class="card border-0 shadow-sm">
            <div class="card-body">
              <div class="text-center mb-4">
                <div class="avatar-placeholder mb-3">
                  <span>{{ iniciais }}</span>
                </div>
                <h5 class="mb-1">{{ usuario.nome }}</h5>
                <p class="text-muted mb-0">{{ usuario.email }}</p>
              </div>

              <div class="list-group list-group-flush">
                <button
                  class="list-group-item list-group-item-action"
                  :class="{ active: activeTab === 'perfil' }"
                  @click="activeTab = 'perfil'"
                >
                  <i class="fas fa-user me-2"></i> Meu Perfil
                </button>
                <button
                  class="list-group-item list-group-item-action"
                  :class="{ active: activeTab === 'doacoes' }"
                  @click="activeTab = 'doacoes'"
                >
                  <i class="fas fa-hand-holding-heart me-2"></i> Minhas Doações
                </button>
                <button
                  class="list-group-item list-group-item-action"
                  :class="{ active: activeTab === 'senha' }"
                  @click="activeTab = 'senha'"
                >
                  <i class="fas fa-lock me-2"></i> Alterar Senha
                </button>
              </div>
            </div>
          </div>
        </div>

        <!-- Conteúdo principal -->
        <div class="col-lg-9">
          <!-- Aba de Perfil -->
          <div v-if="activeTab === 'perfil'" class="card border-0 shadow-sm">
            <div class="card-header bg-white py-3">
              <h5 class="mb-0">Informações Pessoais</h5>
            </div>
            <div class="card-body p-4">
              <div
                v-if="successMessage"
                class="alert alert-success alert-dismissible fade show"
                role="alert"
              >
                {{ successMessage }}
                <button
                  type="button"
                  class="btn-close"
                  @click="successMessage = ''"
                ></button>
              </div>

              <div
                v-if="errorMessage"
                class="alert alert-danger alert-dismissible fade show"
                role="alert"
              >
                {{ errorMessage }}
                <button
                  type="button"
                  class="btn-close"
                  @click="errorMessage = ''"
                ></button>
              </div>

              <form @submit.prevent="atualizarPerfil">
                <div class="row mb-3">
                  <div class="col-md-6">
                    <label for="nome" class="form-label">Nome Completo</label>
                    <input
                      type="text"
                      class="form-control"
                      id="nome"
                      v-model="formPerfil.nome"
                      required
                    />
                  </div>
                  <div class="col-md-6">
                    <label for="email" class="form-label">Email</label>
                    <input
                      type="email"
                      class="form-control"
                      id="email"
                      v-model="formPerfil.email"
                      required
                    />
                  </div>
                </div>

                <div class="row mb-3">
                  <div class="col-md-6">
                    <label for="telefone" class="form-label">Telefone</label>
                    <input
                      type="tel"
                      class="form-control"
                      id="telefone"
                      v-model="formPerfil.telefone"
                    />
                  </div>
                  <div class="col-md-6">
                    <label for="cpf" class="form-label">CPF</label>
                    <input
                      type="text"
                      class="form-control"
                      id="cpf"
                      v-model="formPerfil.cpf"
                      disabled
                    />
                    <small class="form-text text-muted"
                      >O CPF não pode ser alterado.</small
                    >
                  </div>
                </div>

                <div class="mb-3">
                  <label for="endereco" class="form-label">Endereço</label>
                  <input
                    type="text"
                    class="form-control"
                    id="endereco"
                    v-model="formPerfil.endereco"
                  />
                </div>

                <div class="d-flex justify-content-end">
                  <button
                    type="submit"
                    class="btn btn-primary"
                    :disabled="loadingPerfil"
                  >
                    <span
                      v-if="loadingPerfil"
                      class="spinner-border spinner-border-sm me-2"
                      role="status"
                    ></span>
                    Salvar Alterações
                  </button>
                </div>
              </form>
            </div>
          </div>

          <!-- Aba de Doações -->
          <div v-if="activeTab === 'doacoes'" class="card border-0 shadow-sm">
            <div class="card-header bg-white py-3">
              <h5 class="mb-0">Minhas Doações</h5>
            </div>
            <div class="card-body p-4">
              <div v-if="loadingDoacoes" class="text-center py-5">
                <div class="spinner-border text-primary" role="status">
                  <span class="visually-hidden">Carregando...</span>
                </div>
                <p class="mt-3">Carregando suas doações...</p>
              </div>

              <div v-else-if="doacoes.length === 0" class="text-center py-5">
                <i class="fas fa-donate fa-4x text-muted mb-3"></i>
                <h5>Você ainda não fez nenhuma doação</h5>
                <p class="text-muted">Que tal ajudar uma campanha hoje?</p>
                <router-link to="/portal" class="btn btn-primary mt-2">
                  Ver Campanhas
                </router-link>
              </div>

              <div v-else>
                <div class="table-responsive">
                  <table class="table table-hover">
                    <thead>
                      <tr>
                        <th>Campanha</th>
                        <th>Valor</th>
                        <th>Data</th>
                        <th>Status</th>
                        <th>Ações</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr v-for="(doacao, index) in doacoes" :key="index">
                        <td>{{ doacao.campanha.titulo }}</td>
                        <td>R$ {{ formatarValor(doacao.valor) }}</td>
                        <td>{{ formatarData(doacao.dataHora) }}</td>
                        <td>
                          <span :class="getStatusClass(doacao.status)">
                            {{ doacao.status }}
                          </span>
                        </td>
                        <td>
                          <router-link
                            :to="{
                              name: 'campanha-detalhe',
                              params: { id: doacao.campanha.id },
                            }"
                            class="btn btn-sm btn-outline-primary"
                          >
                            <i class="fas fa-eye"></i>
                          </router-link>
                        </td>
                      </tr>
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
          </div>

          <!-- Aba de Alterar Senha -->
          <div v-if="activeTab === 'senha'" class="card border-0 shadow-sm">
            <div class="card-header bg-white py-3">
              <h5 class="mb-0">Alterar Senha</h5>
            </div>
            <div class="card-body p-4">
              <div
                v-if="successMessageSenha"
                class="alert alert-success alert-dismissible fade show"
                role="alert"
              >
                {{ successMessageSenha }}
                <button
                  type="button"
                  class="btn-close"
                  @click="successMessageSenha = ''"
                ></button>
              </div>

              <div
                v-if="errorMessageSenha"
                class="alert alert-danger alert-dismissible fade show"
                role="alert"
              >
                {{ errorMessageSenha }}
                <button
                  type="button"
                  class="btn-close"
                  @click="errorMessageSenha = ''"
                ></button>
              </div>

              <form @submit.prevent="alterarSenha">
                <div class="mb-3">
                  <label for="senhaAtual" class="form-label">Senha Atual</label>
                  <input
                    type="password"
                    class="form-control"
                    id="senhaAtual"
                    v-model="formSenha.senhaAtual"
                    required
                  />
                </div>

                <div class="mb-3">
                  <label for="novaSenha" class="form-label">Nova Senha</label>
                  <input
                    type="password"
                    class="form-control"
                    id="novaSenha"
                    v-model="formSenha.novaSenha"
                    required
                    minlength="6"
                  />
                  <small class="form-text text-muted"
                    >A senha deve ter pelo menos 6 caracteres.</small
                  >
                </div>

                <div class="mb-3">
                  <label for="confirmarSenha" class="form-label"
                    >Confirmar Nova Senha</label
                  >
                  <input
                    type="password"
                    class="form-control"
                    id="confirmarSenha"
                    v-model="formSenha.confirmarSenha"
                    required
                  />
                </div>

                <div class="d-flex justify-content-end">
                  <button
                    type="submit"
                    class="btn btn-primary"
                    :disabled="loadingSenha || !senhasIguais || !senhaValida"
                  >
                    <span
                      v-if="loadingSenha"
                      class="spinner-border spinner-border-sm me-2"
                      role="status"
                    ></span>
                    Alterar Senha
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from "vue";
import { useStore } from "vuex";
import axios from "axios";
import { formatarValor, formatarData } from "../utils";

const store = useStore();
const activeTab = ref("perfil");
const usuario = ref(store.getters.currentUser || {});
const doacoes = ref([]);
const loadingDoacoes = ref(false);
const loadingPerfil = ref(false);
const loadingSenha = ref(false);
const successMessage = ref("");
const errorMessage = ref("");
const successMessageSenha = ref("");
const errorMessageSenha = ref("");

const formPerfil = reactive({
  nome: "",
  email: "",
  telefone: "",
  cpf: "",
  endereco: "",
});
const formSenha = reactive({
  senhaAtual: "",
  novaSenha: "",
  confirmarSenha: "",
});

const iniciais = computed(() => {
  if (!usuario.value.nome) return "?";
  return usuario.value.nome
    .split(" ")
    .map((n) => n[0])
    .slice(0, 2)
    .join("")
    .toUpperCase();
});

const senhasIguais = computed(
  () => formSenha.novaSenha === formSenha.confirmarSenha
);
const senhaValida = computed(() => formSenha.novaSenha.length >= 6);

const carregarDadosUsuario = async () => {
  try {
    const resp = await axios.get(
      `${process.env.VUE_APP_API_BASE_URL}/api/usuarios/atual`
    );
    usuario.value = resp.data;
    formPerfil.nome = usuario.value.nome || "";
    formPerfil.email = usuario.value.email || "";
    formPerfil.telefone = usuario.value.telefone || "";
    formPerfil.cpf = usuario.value.cpfCnpj || "";
    formPerfil.endereco = usuario.value.endereco || "";
  } catch (e) {
    console.error("Erro ao carregar usuario:", e);
  }
};

const carregarDoacoes = async () => {
  loadingDoacoes.value = true;
  try {
    const resp = await axios.get(
      `${process.env.VUE_APP_API_BASE_URL}/api/doacoes/usuario/atual`
    );
    doacoes.value = resp.data;
  } catch (e) {
    console.error("Erro ao carregar doacoes:", e);
  } finally {
    loadingDoacoes.value = false;
  }
};

const getStatusClass = (status) =>
  ({
    CONFIRMADA: "badge bg-success",
    PENDENTE: "badge bg-secondary",
    CANCELADA: "badge bg-danger",
  }[status] || "badge bg-secondary");

const atualizarPerfil = async () => {
  loadingPerfil.value = true;
  successMessage.value = "";
  errorMessage.value = "";
  const usuarioAtualizado = {
    nome: formPerfil.nome,
    email: formPerfil.email,
    telefone: formPerfil.telefone,
    endereco: formPerfil.endereco,
  };
  try {
    await axios.put(
      `${process.env.VUE_APP_API_BASE_URL}/api/usuarios/${usuario.value.id}`,
      usuarioAtualizado
    );
    loadingPerfil.value = false;
    successMessage.value = "Perfil atualizado com sucesso!";
    const updatedUser = { ...usuario.value, ...usuarioAtualizado };
    localStorage.setItem("user", JSON.stringify(updatedUser));
    store.commit("auth_success", {
      token: store.state.token,
      user: updatedUser,
    });
    usuario.value = updatedUser;
  } catch (e) {
    console.error("Erro ao atualizar perfil:", e);
    loadingPerfil.value = false;
    errorMessage.value =
      e.response?.data?.message || "Erro ao atualizar perfil. Tente novamente.";
  }
};

const alterarSenha = async () => {
  if (!senhasIguais.value) {
    errorMessageSenha.value = "As senhas nao coincidem.";
    return;
  }
  if (!senhaValida.value) {
    errorMessageSenha.value = "A senha deve ter pelo menos 6 caracteres.";
    return;
  }
  loadingSenha.value = true;
  successMessageSenha.value = "";
  errorMessageSenha.value = "";
  try {
    await axios.put(
      `${process.env.VUE_APP_API_BASE_URL}/api/usuarios/${usuario.value.id}/senha`,
      { senhaAtual: formSenha.senhaAtual, novaSenha: formSenha.novaSenha }
    );
    loadingSenha.value = false;
    successMessageSenha.value = "Senha alterada com sucesso!";
    formSenha.senhaAtual = formSenha.novaSenha = formSenha.confirmarSenha = "";
  } catch (e) {
    console.error("Erro ao alterar senha:", e);
    loadingSenha.value = false;
    errorMessageSenha.value =
      e.response?.data?.message || "Erro ao alterar senha. Tente novamente.";
  }
};

onMounted(() => {
  carregarDadosUsuario();
  carregarDoacoes();
});
</script>

<style scoped>
.perfil-view {
  background-color: #f8f9fa;
  min-height: 100vh;
}

.avatar-placeholder {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  background-color: #007bff;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5rem;
  font-weight: bold;
  margin: 0 auto;
}

.list-group-item {
  border: none;
  padding: 0.75rem 1rem;
  margin-bottom: 5px;
  border-radius: 0.5rem !important;
  transition: all 0.2s;
}

.list-group-item:hover {
  background-color: rgba(0, 123, 255, 0.1);
}

.list-group-item.active {
  background-color: #007bff;
  color: white;
}

.card {
  border-radius: 0.5rem;
  overflow: hidden;
}

.btn-outline-primary {
  border-radius: 0.5rem;
}
</style>
