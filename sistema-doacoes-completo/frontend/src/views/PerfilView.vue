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

<script>
import axios from "axios";

export default {
  name: "PerfilView",
  data() {
    return {
      activeTab: "perfil",
      usuario: this.$store.getters.currentUser || {},
      doacoes: [],
      loadingDoacoes: false,
      loadingPerfil: false,
      loadingSenha: false,
      successMessage: "",
      errorMessage: "",
      successMessageSenha: "",
      errorMessageSenha: "",
      formPerfil: {
        nome: "",
        email: "",
        telefone: "",
        cpf: "",
        endereco: "",
      },
      formSenha: {
        senhaAtual: "",
        novaSenha: "",
        confirmarSenha: "",
      },
    };
  },
  computed: {
    iniciais() {
      if (!this.usuario.nome) return "?";
      return this.usuario.nome
        .split(" ")
        .map((n) => n[0])
        .slice(0, 2)
        .join("")
        .toUpperCase();
    },
    senhasIguais() {
      return this.formSenha.novaSenha === this.formSenha.confirmarSenha;
    },
    senhaValida() {
      return this.formSenha.novaSenha.length >= 6;
    },
  },
  created() {
    this.carregarDadosUsuario();
    this.carregarDoacoes();
  },
  methods: {
    carregarDadosUsuario() {
      axios
        .get(`${process.env.VUE_APP_API_BASE_URL}/api/usuarios/atual`)
        .then((response) => {
          this.usuario = response.data;

          this.formPerfil.nome = this.usuario.nome || "";
          this.formPerfil.email = this.usuario.email || "";
          this.formPerfil.telefone = this.usuario.telefone || "";
          this.formPerfil.cpf = this.usuario.cpfCnpj || "";
          this.formPerfil.endereco = this.usuario.endereco || "";
        })
        .catch((error) => {
          console.error("Erro ao carregar usuario:", error);
        });
    },
    carregarDoacoes() {
      this.loadingDoacoes = true;

      axios
        .get(`${process.env.VUE_APP_API_BASE_URL}/api/doacoes/usuario/atual`)
        .then((response) => {
          this.doacoes = response.data;
          this.loadingDoacoes = false;
        })
        .catch((error) => {
          console.error("Erro ao carregar doações:", error);
          this.loadingDoacoes = false;
        });
    },
    atualizarPerfil() {
      this.loadingPerfil = true;
      this.successMessage = "";
      this.errorMessage = "";

      const usuarioAtualizado = {
        nome: this.formPerfil.nome,
        email: this.formPerfil.email,
        telefone: this.formPerfil.telefone,
        endereco: this.formPerfil.endereco,
      };

      axios
        .put(
          `${process.env.VUE_APP_API_BASE_URL}/api/usuarios/${this.usuario.id}`,
          usuarioAtualizado
        )
        .then(() => {
          this.loadingPerfil = false;
          this.successMessage = "Perfil atualizado com sucesso!";

          const updatedUser = {
            ...this.usuario,
            nome: usuarioAtualizado.nome,
            email: usuarioAtualizado.email,
            telefone: usuarioAtualizado.telefone,
            endereco: usuarioAtualizado.endereco,
          };

          localStorage.setItem("user", JSON.stringify(updatedUser));
          this.$store.commit("auth_success", {
            token: this.$store.state.token,
            user: updatedUser,
          });

          this.usuario = updatedUser;
        })
        .catch((error) => {
          console.error("Erro ao atualizar perfil:", error);
          this.loadingPerfil = false;
          this.errorMessage =
            error.response?.data?.message ||
            "Erro ao atualizar perfil. Tente novamente.";
        });
    },
    alterarSenha() {
      if (!this.senhasIguais) {
        this.errorMessageSenha = "As senhas não coincidem.";
        return;
      }

      if (!this.senhaValida) {
        this.errorMessageSenha = "A senha deve ter pelo menos 6 caracteres.";
        return;
      }

      this.loadingSenha = true;
      this.successMessageSenha = "";
      this.errorMessageSenha = "";

      const senhas = {
        senhaAtual: this.formSenha.senhaAtual,
        novaSenha: this.formSenha.novaSenha,
      };

      axios
        .put(
          `${process.env.VUE_APP_API_BASE_URL}/api/usuarios/${this.usuario.id}/senha`,
          senhas
        )
        .then(() => {
          this.loadingSenha = false;
          this.successMessageSenha = "Senha alterada com sucesso!";

          this.formSenha = {
            senhaAtual: "",
            novaSenha: "",
            confirmarSenha: "",
          };
        })
        .catch((error) => {
          console.error("Erro ao alterar senha:", error);
          this.loadingSenha = false;
          this.errorMessageSenha =
            error.response?.data?.message ||
            "Erro ao alterar senha. Tente novamente.";
        });
    },
    formatarValor(valor) {
      if (valor === undefined || valor === null) {
        return "0,00";
      }
      return valor.toLocaleString("pt-BR", {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2,
      });
    },
    formatarData(data) {
      if (!data) return "Data não disponível";

      const options = { day: "2-digit", month: "2-digit", year: "numeric" };
      return new Date(data).toLocaleDateString("pt-BR", options);
    },
    getStatusClass(status) {
      const classes = {
        CONFIRMADA: "badge bg-success",
        PENDENTE: "badge bg-warning text-dark",
        CANCELADA: "badge bg-danger",
      };
      return classes[status] || "badge bg-secondary";
    },
  },
};
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
