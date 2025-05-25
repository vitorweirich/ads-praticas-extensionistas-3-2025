<template>
  <div class="perfil-view">
    <div class="row">
      <div class="col-md-4 mb-4">
        <div class="card">
          <div class="card-header bg-primary text-white">
            <h5 class="mb-0">Meu Perfil</h5>
          </div>
          <div class="card-body">
            <div class="text-center mb-4">
              <div class="avatar-circle mb-3">
                <span class="avatar-initials">{{ iniciais }}</span>
              </div>
              <h4>{{ currentUser.nome }}</h4>
              <p class="text-muted">{{ currentUser.email }}</p>
              <span class="badge bg-primary">{{ tipoUsuario }}</span>
            </div>

            <div class="mb-3">
              <button
                class="btn btn-outline-primary w-100"
                @click="editarPerfil"
              >
                <i class="fas fa-user-edit"></i> Editar Perfil
              </button>
            </div>

            <div class="mb-3">
              <button
                class="btn btn-outline-secondary w-100"
                @click="alterarSenha"
              >
                <i class="fas fa-key"></i> Alterar Senha
              </button>
            </div>
          </div>
        </div>
      </div>

      <div class="col-md-8">
        <div class="card mb-4">
          <div class="card-header bg-primary text-white">
            <h5 class="mb-0">Informações Pessoais</h5>
          </div>
          <div class="card-body">
            <form v-if="modoEdicao" @submit.prevent="salvarPerfil">
              <div class="row">
                <div class="col-md-6 mb-3">
                  <label for="nome" class="form-label">Nome Completo</label>
                  <input
                    type="text"
                    class="form-control"
                    id="nome"
                    v-model="perfilEditado.nome"
                    required
                  />
                </div>
                <div class="col-md-6 mb-3">
                  <label for="email" class="form-label">Email</label>
                  <input
                    type="email"
                    class="form-control"
                    id="email"
                    v-model="perfilEditado.email"
                    required
                    disabled
                  />
                </div>
              </div>

              <div class="row">
                <div class="col-md-6 mb-3">
                  <label for="telefone" class="form-label">Telefone</label>
                  <input
                    type="tel"
                    class="form-control"
                    id="telefone"
                    v-model="perfilEditado.telefone"
                  />
                </div>
                <div class="col-md-6 mb-3">
                  <label for="cpfCnpj" class="form-label">CPF/CNPJ</label>
                  <input
                    type="text"
                    class="form-control"
                    id="cpfCnpj"
                    v-model="perfilEditado.cpfCnpj"
                  />
                </div>
              </div>

              <div class="mb-3">
                <label for="endereco" class="form-label">Endereço</label>
                <textarea
                  class="form-control"
                  id="endereco"
                  v-model="perfilEditado.endereco"
                  rows="2"
                ></textarea>
              </div>

              <div class="d-flex justify-content-end">
                <button
                  type="button"
                  class="btn btn-secondary me-2"
                  @click="cancelarEdicao"
                >
                  Cancelar
                </button>
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
                  Salvar
                </button>
              </div>
            </form>

            <div v-else>
              <div class="row mb-3">
                <div class="col-md-6">
                  <p class="mb-1"><strong>Telefone:</strong></p>
                  <p>{{ currentUser.telefone || "Não informado" }}</p>
                </div>
                <div class="col-md-6">
                  <p class="mb-1"><strong>CPF/CNPJ:</strong></p>
                  <p>{{ currentUser.cpfCnpj || "Não informado" }}</p>
                </div>
              </div>

              <div class="mb-3">
                <p class="mb-1"><strong>Endereço:</strong></p>
                <p>{{ currentUser.endereco || "Não informado" }}</p>
              </div>

              <div class="mb-3">
                <p class="mb-1"><strong>Data de Cadastro:</strong></p>
                <p>{{ formatarData(currentUser.dataCadastro) }}</p>
              </div>
            </div>
          </div>
        </div>

        <div class="card">
          <div class="card-header bg-primary text-white">
            <h5 class="mb-0">Minhas Doações</h5>
          </div>
          <div class="card-body p-0">
            <div class="table-responsive">
              <table class="table table-hover mb-0">
                <thead>
                  <tr>
                    <th>Campanha</th>
                    <th>Valor</th>
                    <th>Data</th>
                    <th>Status</th>
                    <th>Comprovante</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-if="minhasDoacoes.length === 0">
                    <td colspan="5" class="text-center py-3">
                      <p v-if="loadingDoacoes" class="mb-0">
                        <span
                          class="spinner-border spinner-border-sm"
                          role="status"
                          aria-hidden="true"
                        ></span>
                        Carregando suas doações...
                      </p>
                      <p v-else class="mb-0">
                        Você ainda não realizou nenhuma doação.
                      </p>
                    </td>
                  </tr>
                  <tr v-for="doacao in minhasDoacoes" :key="doacao.id">
                    <td>{{ doacao.campanha.titulo }}</td>
                    <td>R$ {{ formatarValor(doacao.valor) }}</td>
                    <td>{{ formatarData(doacao.dataHora) }}</td>
                    <td>
                      <span :class="getStatusClass(doacao.status)">{{
                        doacao.status
                      }}</span>
                    </td>
                    <td>
                      <button
                        v-if="doacao.comprovante"
                        class="btn btn-sm btn-outline-primary"
                      >
                        <i class="fas fa-file-alt"></i> Ver
                      </button>
                      <span v-else>-</span>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div
      class="modal fade"
      id="alterarSenhaModal"
      tabindex="-1"
      aria-labelledby="alterarSenhaModalLabel"
      aria-hidden="true"
    >
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="alterarSenhaModalLabel">
              Alterar Senha
            </h5>
            <button
              type="button"
              class="btn-close"
              data-bs-dismiss="modal"
              aria-label="Close"
            ></button>
          </div>
          <div class="modal-body">
            <form @submit.prevent="salvarSenha">
              <div class="mb-3">
                <label for="senhaAtual" class="form-label">Senha Atual</label>
                <input
                  type="password"
                  class="form-control"
                  id="senhaAtual"
                  v-model="senhas.atual"
                  required
                />
              </div>
              <div class="mb-3">
                <label for="novaSenha" class="form-label">Nova Senha</label>
                <input
                  type="password"
                  class="form-control"
                  id="novaSenha"
                  v-model="senhas.nova"
                  required
                />
                <small class="form-text text-muted"
                  >A senha deve ter pelo menos 6 caracteres</small
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
                  v-model="senhas.confirmar"
                  required
                />
              </div>
              <div class="d-flex justify-content-end">
                <button
                  type="button"
                  class="btn btn-secondary me-2"
                  data-bs-dismiss="modal"
                >
                  Cancelar
                </button>
                <button
                  type="submit"
                  class="btn btn-primary"
                  :disabled="loadingSenha"
                >
                  <span
                    v-if="loadingSenha"
                    class="spinner-border spinner-border-sm"
                    role="status"
                    aria-hidden="true"
                  ></span>
                  Salvar
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import * as bootstrap from "bootstrap";

export default {
  name: "PerfilView",
  data() {
    return {
      modoEdicao: false,
      perfilEditado: {},
      loading: false,
      loadingDoacoes: true,
      loadingSenha: false,
      minhasDoacoes: [],
      senhas: {
        atual: "",
        nova: "",
        confirmar: "",
      },
    };
  },
  computed: {
    ...mapGetters(["currentUser", "isAdmin"]),
    iniciais() {
      if (!this.currentUser.nome) return "?";
      return this.currentUser.nome
        .split(" ")
        .map((n) => n[0])
        .slice(0, 2)
        .join("")
        .toUpperCase();
    },
    tipoUsuario() {
      return this.isAdmin ? "Administrador" : "Doador";
    },
  },
  methods: {
    formatarValor(valor) {
      return valor
        ? valor.toLocaleString("pt-BR", {
            minimumFractionDigits: 2,
            maximumFractionDigits: 2,
          })
        : "0,00";
    },
    formatarData(data) {
      if (!data) return "-";
      const dataObj = new Date(data);
      return dataObj.toLocaleDateString("pt-BR");
    },
    getStatusClass(status) {
      const classes = {
        CONFIRMADA: "badge bg-success",
        PENDENTE: "badge bg-warning text-dark",
        CANCELADA: "badge bg-danger",
      };
      return classes[status] || "badge bg-secondary";
    },
    editarPerfil() {
      this.perfilEditado = { ...this.currentUser };
      this.modoEdicao = true;
    },
    cancelarEdicao() {
      this.modoEdicao = false;
    },
    salvarPerfil() {
      this.loading = true;

      setTimeout(() => {
        // TODO: Revisar
        // this.$store.commit('update_user', this.perfilEditado)

        this.modoEdicao = false;
        this.loading = false;

        alert("Perfil atualizado com sucesso!");
      }, 1000);
    },
    alterarSenha() {
      this.senhas = {
        atual: "",
        nova: "",
        confirmar: "",
      };

      const myModal = new bootstrap.Modal(
        document.getElementById("alterarSenhaModal")
      );
      myModal.show();
    },
    salvarSenha() {
      if (this.senhas.nova !== this.senhas.confirmar) {
        alert("As senhas não coincidem!");
        return;
      }

      if (this.senhas.nova.length < 6) {
        alert("A nova senha deve ter pelo menos 6 caracteres!");
        return;
      }

      this.loadingSenha = true;

      // TODO: Implementar chamada à API para alterar a senha
      setTimeout(() => {
        this.loadingSenha = false;

        const myModal = bootstrap.Modal.getInstance(
          document.getElementById("alterarSenhaModal")
        );
        myModal.hide();

        alert("Senha alterada com sucesso!");
      }, 1000);
    },
    carregarDoacoes() {
      this.loadingDoacoes = true;

      // TODO: Buscar dados reais da API
      setTimeout(() => {
        this.minhasDoacoes = [
          {
            id: 1,
            campanha: { id: 1, titulo: "Campanha de Saúde" },
            valor: 100.0,
            dataHora: "2025-05-15T10:30:00",
            status: "CONFIRMADA",
            comprovante: "https://example.com/comprovante1.pdf",
          },
          {
            id: 2,
            campanha: { id: 2, titulo: "Educação para Todos" },
            valor: 50.0,
            dataHora: "2025-05-10T14:45:00",
            status: "CONFIRMADA",
            comprovante: "https://example.com/comprovante2.pdf",
          },
        ];

        this.loadingDoacoes = false;
      }, 1000);
    },
  },
  created() {
    this.carregarDoacoes();
  },
};
</script>

<style scoped>
.perfil-view {
  padding-top: 20px;
  padding-bottom: 30px;
}

.avatar-circle {
  width: 100px;
  height: 100px;
  background-color: #007bff;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 0 auto;
}

.avatar-initials {
  font-size: 40px;
  color: white;
  font-weight: bold;
}
</style>
