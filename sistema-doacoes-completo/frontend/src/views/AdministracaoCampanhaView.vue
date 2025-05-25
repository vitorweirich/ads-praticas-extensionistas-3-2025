<template>
  <div class="admin-campanhas-view">
    <div class="container py-5">
      <div class="row mb-4">
        <div class="col-12">
          <div class="d-flex justify-content-between align-items-center">
            <h1 class="mb-0">Administração de Campanhas</h1>
            <router-link to="/admin" class="btn btn-outline-secondary">
              <i class="fas fa-arrow-left me-2"></i> Voltar ao Painel
            </router-link>
          </div>
        </div>
      </div>

      <!-- Loading state -->
      <div v-if="loading" class="text-center py-5">
        <div class="spinner-border text-primary" role="status">
          <span class="visually-hidden">Carregando...</span>
        </div>
        <p class="mt-3">Carregando campanhas...</p>
      </div>

      <div v-else>
        <!-- Filtros -->
        <div class="row mb-4">
          <div class="col-md-6">
            <div class="input-group">
              <input
                type="text"
                class="form-control"
                placeholder="Buscar campanhas..."
                v-model="filtro.termo"
                @input="filtrarCampanhas"
              />
              <button
                class="btn btn-outline-secondary"
                type="button"
                @click="filtrarCampanhas"
              >
                <i class="fas fa-search"></i>
              </button>
            </div>
          </div>
          <div class="col-md-3">
            <select
              class="form-select"
              v-model="filtro.status"
              @change="filtrarCampanhas"
            >
              <option value="">Todos os status</option>
              <option value="ATIVA">Ativa</option>
              <option value="FINALIZADA">Finalizada</option>
              <option value="CANCELADA">Cancelada</option>
            </select>
          </div>
          <div class="col-md-3">
            <select
              class="form-select"
              v-model="filtro.categoria"
              @change="filtrarCampanhas"
            >
              <option value="">Todas as categorias</option>
              <option value="saude">Saúde</option>
              <option value="educacao">Educação</option>
              <option value="emergencia">Emergência</option>
              <option value="social">Social</option>
              <option value="ambiental">Ambiental</option>
            </select>
          </div>
        </div>

        <!-- Lista de campanhas -->
        <div class="row">
          <div
            v-if="campanhasFiltradas.length === 0"
            class="col-12 text-center py-5"
          >
            <i class="fas fa-search fa-3x text-muted mb-3"></i>
            <h4>Nenhuma campanha encontrada</h4>
            <p class="text-muted">Tente ajustar os filtros de busca</p>
          </div>

          <div
            v-for="campanha in campanhasFiltradas"
            :key="campanha.id"
            class="col-md-6 col-lg-4 mb-4"
          >
            <div class="card h-100 shadow-sm">
              <div class="position-relative">
                <img
                  :src="
                    campanha.imagemCapa ||
                    'https://via.placeholder.com/400x200?text=Imagem+da+Campanha'
                  "
                  class="card-img-top"
                  alt="Imagem da campanha"
                  style="height: 200px; object-fit: cover"
                />
                <span
                  :class="getStatusClass(campanha.status)"
                  class="position-absolute top-0 end-0 m-2"
                >
                  {{ campanha.status }}
                </span>
              </div>
              <div class="card-body">
                <h5 class="card-title">{{ campanha.titulo }}</h5>
                <p class="card-text text-muted">
                  {{ truncateText(campanha.descricao, 100) }}
                </p>

                <div class="progress mb-3" style="height: 10px">
                  <div
                    class="progress-bar bg-success"
                    role="progressbar"
                    :style="{ width: calcularProgresso(campanha) + '%' }"
                    :aria-valuenow="calcularProgresso(campanha)"
                    aria-valuemin="0"
                    aria-valuemax="100"
                  ></div>
                </div>

                <div class="d-flex justify-content-between mb-3">
                  <small>
                    <strong>Meta:</strong> R$
                    {{ formatarValor(campanha.metaFinanceira) }}
                  </small>
                  <small>
                    <strong>Arrecadado:</strong> R$
                    {{ formatarValor(campanha.valorArrecadado) }}
                  </small>
                </div>

                <div class="d-flex justify-content-between align-items-center">
                  <span class="badge bg-primary">{{ campanha.categoria }}</span>
                  <button
                    class="btn btn-sm btn-outline-primary"
                    @click="verCampanha(campanha)"
                  >
                    Gerenciar
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Modal de Detalhes da Campanha -->
    <div
      class="modal fade"
      :class="{ 'show d-block': showModalCampanha }"
      tabindex="-1"
      :style="{ background: showModalCampanha ? 'rgba(0,0,0,0.5)' : '' }"
    >
      <div class="modal-dialog modal-xl">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">Gerenciar Campanha</h5>
            <button
              type="button"
              class="btn-close"
              @click="fecharModalCampanha"
            ></button>
          </div>
          <div class="modal-body">
            <div v-if="campanhaAtual">
              <div class="row mb-4">
                <div class="col-md-8">
                  <img
                    :src="
                      campanhaAtual.imagemCapa ||
                      'https://via.placeholder.com/800x400?text=Imagem+da+Campanha'
                    "
                    class="img-fluid rounded"
                    alt="Imagem da campanha"
                  />
                </div>
                <div class="col-md-4">
                  <div class="card h-100">
                    <div class="card-body">
                      <h5 class="card-title">{{ campanhaAtual.titulo }}</h5>
                      <p class="card-text">
                        <span :class="getStatusClass(campanhaAtual.status)">{{
                          campanhaAtual.status
                        }}</span>
                      </p>
                      <p class="card-text">
                        <strong>Categoria:</strong>
                        {{ campanhaAtual.categoria }}
                      </p>
                      <p class="card-text">
                        <strong>Meta:</strong> R$
                        {{ formatarValor(campanhaAtual.metaFinanceira) }}
                      </p>
                      <p class="card-text">
                        <strong>Arrecadado:</strong> R$
                        {{ formatarValor(campanhaAtual.valorArrecadado) }}
                      </p>
                      <p class="card-text">
                        <strong>Progresso:</strong>
                        {{ calcularProgresso(campanhaAtual) }}%
                      </p>
                      <p class="card-text">
                        <strong>Data de Término:</strong>
                        {{ formatarData(campanhaAtual.dataTermino) }}
                      </p>
                      <div class="d-grid gap-2">
                        <button
                          class="btn btn-outline-primary"
                          @click="editarCampanha"
                        >
                          <i class="fas fa-edit me-2"></i> Editar Campanha
                        </button>
                        <button
                          class="btn btn-outline-secondary"
                          :class="{
                            'btn-outline-success':
                              campanhaAtual.status !== 'ATIVA',
                            'btn-outline-danger':
                              campanhaAtual.status === 'ATIVA',
                          }"
                          @click="alterarStatusCampanha"
                        >
                          <i
                            class="fas"
                            :class="
                              campanhaAtual.status === 'ATIVA'
                                ? 'fa-pause'
                                : 'fa-play'
                            "
                          ></i>
                          {{
                            campanhaAtual.status === "ATIVA"
                              ? " Pausar Campanha"
                              : " Ativar Campanha"
                          }}
                        </button>
                      </div>
                    </div>
                  </div>
                </div>
              </div>

              <!-- Abas de navegação -->
              <ul class="nav nav-tabs" id="campanhaTab" role="tablist">
                <li class="nav-item" role="presentation">
                  <button
                    class="nav-link active"
                    id="doacoes-tab"
                    data-bs-toggle="tab"
                    data-bs-target="#doacoes"
                    type="button"
                    role="tab"
                    aria-controls="doacoes"
                    aria-selected="true"
                  >
                    Doações Pendentes
                    <span
                      v-if="doacoesPendentes.length > 0"
                      class="badge bg-danger ms-2"
                    >
                      {{ doacoesPendentes.length }}
                    </span>
                  </button>
                </li>
                <li class="nav-item" role="presentation">
                  <button
                    class="nav-link"
                    id="todas-doacoes-tab"
                    data-bs-toggle="tab"
                    data-bs-target="#todas-doacoes"
                    type="button"
                    role="tab"
                    aria-controls="todas-doacoes"
                    aria-selected="false"
                  >
                    Todas as Doações
                  </button>
                </li>
                <li class="nav-item" role="presentation">
                  <button
                    class="nav-link"
                    id="detalhes-tab"
                    data-bs-toggle="tab"
                    data-bs-target="#detalhes"
                    type="button"
                    role="tab"
                    aria-controls="detalhes"
                    aria-selected="false"
                  >
                    Detalhes da Campanha
                  </button>
                </li>
              </ul>

              <div
                class="tab-content p-3 border border-top-0 rounded-bottom"
                id="campanhaTabContent"
              >
                <!-- Aba de Doações Pendentes -->
                <div
                  class="tab-pane fade show active"
                  id="doacoes"
                  role="tabpanel"
                  aria-labelledby="doacoes-tab"
                >
                  <div v-if="loadingDoacoes" class="text-center py-4">
                    <div
                      class="spinner-border spinner-border-sm text-primary"
                      role="status"
                    ></div>
                    <span class="ms-2">Carregando doações...</span>
                  </div>

                  <div
                    v-else-if="doacoesPendentes.length === 0"
                    class="text-center py-4"
                  >
                    <i class="fas fa-check-circle fa-3x text-success mb-3"></i>
                    <h5>Não há doações pendentes</h5>
                    <p class="text-muted">Todas as doações foram processadas</p>
                  </div>

                  <div v-else>
                    <div class="alert alert-warning">
                      <i class="fas fa-exclamation-triangle me-2"></i>
                      Existem
                      <strong>{{ doacoesPendentes.length }}</strong> doações
                      pendentes que precisam de aprovação.
                    </div>

                    <div class="table-responsive">
                      <table class="table table-hover">
                        <thead>
                          <tr>
                            <th>Doador</th>
                            <th>Valor</th>
                            <th>Data</th>
                            <th>Forma de Pagamento</th>
                            <th>Ações</th>
                          </tr>
                        </thead>
                        <tbody>
                          <tr
                            v-for="doacao in doacoesPendentes"
                            :key="doacao.id"
                          >
                            <td>
                              {{
                                doacao.anonimo
                                  ? "Anônimo"
                                  : doacao.doador
                                  ? doacao.doador.nome
                                  : "N/A"
                              }}
                            </td>
                            <td>R$ {{ formatarValor(doacao.valor) }}</td>
                            <td>{{ formatarData(doacao.dataHora) }}</td>
                            <td>{{ doacao.formaPagamento }}</td>
                            <td>
                              <div class="btn-group">
                                <button
                                  class="btn btn-sm btn-success"
                                  @click="confirmarDoacao(doacao)"
                                  :disabled="processandoDoacao === doacao.id"
                                >
                                  <span
                                    v-if="processandoDoacao === doacao.id"
                                    class="spinner-border spinner-border-sm"
                                    role="status"
                                  ></span>
                                  <i v-else class="fas fa-check"></i>
                                  Aprovar
                                </button>
                                <button
                                  class="btn btn-sm btn-danger"
                                  @click="recusarDoacao(doacao)"
                                  :disabled="processandoDoacao === doacao.id"
                                >
                                  <i class="fas fa-times"></i>
                                  Recusar
                                </button>
                              </div>
                            </td>
                          </tr>
                        </tbody>
                      </table>
                    </div>
                  </div>
                </div>

                <!-- Aba de Todas as Doações -->
                <div
                  class="tab-pane fade"
                  id="todas-doacoes"
                  role="tabpanel"
                  aria-labelledby="todas-doacoes-tab"
                >
                  <div v-if="loadingDoacoes" class="text-center py-4">
                    <div
                      class="spinner-border spinner-border-sm text-primary"
                      role="status"
                    ></div>
                    <span class="ms-2">Carregando doações...</span>
                  </div>

                  <div
                    v-else-if="doacoesCampanha.length === 0"
                    class="text-center py-4"
                  >
                    <i class="fas fa-donate fa-3x text-muted mb-3"></i>
                    <h5>Nenhuma doação encontrada</h5>
                    <p class="text-muted">
                      Esta campanha ainda não recebeu doações
                    </p>
                  </div>

                  <div v-else>
                    <div
                      class="d-flex justify-content-between align-items-center mb-3"
                    >
                      <h5 class="mb-0">Histórico de Doações</h5>
                      <div class="input-group" style="max-width: 300px">
                        <input
                          type="text"
                          class="form-control"
                          placeholder="Buscar doações..."
                          v-model="filtroDoacoes"
                        />
                        <button class="btn btn-outline-secondary" type="button">
                          <i class="fas fa-search"></i>
                        </button>
                      </div>
                    </div>

                    <div class="table-responsive">
                      <table class="table table-hover">
                        <thead>
                          <tr>
                            <th>Doador</th>
                            <th>Valor</th>
                            <th>Data</th>
                            <th>Forma de Pagamento</th>
                            <th>Status</th>
                            <th>Ações</th>
                          </tr>
                        </thead>
                        <tbody>
                          <tr
                            v-for="doacao in doacoesFiltradas"
                            :key="doacao.id"
                          >
                            <td>
                              {{
                                doacao.anonimo
                                  ? "Anônimo"
                                  : doacao.doador
                                  ? doacao.doador.nome
                                  : "N/A"
                              }}
                            </td>
                            <td>R$ {{ formatarValor(doacao.valor) }}</td>
                            <td>{{ formatarData(doacao.dataHora) }}</td>
                            <td>{{ doacao.formaPagamento }}</td>
                            <td>
                              <span :class="getStatusClass(doacao.status)">{{
                                doacao.status
                              }}</span>
                            </td>
                            <td>
                              <div class="btn-group">
                                <button
                                  class="btn btn-sm btn-outline-primary"
                                  @click="verDetalhesDoacao(doacao)"
                                >
                                  <i class="fas fa-eye"></i>
                                </button>
                                <button
                                  v-if="doacao.status === 'PENDENTE'"
                                  class="btn btn-sm btn-outline-success"
                                  @click="confirmarDoacao(doacao)"
                                  :disabled="processandoDoacao === doacao.id"
                                >
                                  <i class="fas fa-check"></i>
                                </button>
                                <button
                                  v-if="doacao.status !== 'CANCELADA'"
                                  class="btn btn-sm btn-outline-danger"
                                  @click="recusarDoacao(doacao)"
                                  :disabled="processandoDoacao === doacao.id"
                                >
                                  <i class="fas fa-times"></i>
                                </button>
                              </div>
                            </td>
                          </tr>
                        </tbody>
                      </table>
                    </div>
                  </div>
                </div>

                <!-- Aba de Detalhes da Campanha -->
                <div
                  class="tab-pane fade"
                  id="detalhes"
                  role="tabpanel"
                  aria-labelledby="detalhes-tab"
                >
                  <div class="row">
                    <div class="col-md-8">
                      <h5 class="mb-3">Descrição</h5>
                      <div class="card mb-4">
                        <div class="card-body">
                          <p
                            class="card-text"
                            v-html="campanhaAtual.descricao"
                          ></p>
                        </div>
                      </div>

                      <h5 class="mb-3">Beneficiários</h5>
                      <div class="card">
                        <div class="card-body">
                          <p class="card-text">
                            {{
                              campanhaAtual.beneficiarios ||
                              "Nenhuma informação sobre beneficiários disponível."
                            }}
                          </p>
                        </div>
                      </div>
                    </div>
                    <div class="col-md-4">
                      <h5 class="mb-3">Estatísticas</h5>
                      <div class="card mb-4">
                        <div class="card-body">
                          <div class="d-flex justify-content-between mb-3">
                            <span>Total de doações:</span>
                            <strong>{{ doacoesCampanha.length }}</strong>
                          </div>
                          <div class="d-flex justify-content-between mb-3">
                            <span>Doações confirmadas:</span>
                            <strong>{{ doacoesConfirmadas.length }}</strong>
                          </div>
                          <div class="d-flex justify-content-between mb-3">
                            <span>Doações pendentes:</span>
                            <strong>{{ doacoesPendentes.length }}</strong>
                          </div>
                          <div class="d-flex justify-content-between mb-3">
                            <span>Doações canceladas:</span>
                            <strong>{{ doacoesCanceladas.length }}</strong>
                          </div>
                          <div class="d-flex justify-content-between mb-3">
                            <span>Valor médio:</span>
                            <strong>R$ {{ calcularValorMedio() }}</strong>
                          </div>
                        </div>
                      </div>

                      <h5 class="mb-3">Ações</h5>
                      <div class="d-grid gap-2">
                        <button
                          class="btn btn-outline-primary"
                          @click="editarCampanha"
                        >
                          <i class="fas fa-edit me-2"></i> Editar Campanha
                        </button>
                        <button
                          class="btn"
                          :class="{
                            'btn-outline-success':
                              campanhaAtual.status !== 'ATIVA',
                            'btn-outline-danger':
                              campanhaAtual.status === 'ATIVA',
                          }"
                          @click="alterarStatusCampanha"
                        >
                          <i
                            class="fas"
                            :class="
                              campanhaAtual.status === 'ATIVA'
                                ? 'fa-pause'
                                : 'fa-play'
                            "
                          ></i>
                          {{
                            campanhaAtual.status === "ATIVA"
                              ? " Pausar Campanha"
                              : " Ativar Campanha"
                          }}
                        </button>
                        <button
                          class="btn btn-outline-secondary"
                          @click="verAlocacoes"
                        >
                          <i class="fas fa-chart-pie me-2"></i> Ver Alocações
                        </button>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="modal-footer">
            <button
              type="button"
              class="btn btn-secondary"
              @click="fecharModalCampanha"
            >
              Fechar
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "AdminCampanhasView",
  data() {
    return {
      loading: true,
      loadingDoacoes: false,
      campanhas: [],
      campanhasFiltradas: [],
      doacoesCampanha: [],
      campanhaAtual: null,
      showModalCampanha: false,
      processandoDoacao: null,

      filtro: {
        termo: "",
        status: "",
        categoria: "",
      },

      filtroDoacoes: "",
    };
  },
  computed: {
    doacoesPendentes() {
      return this.doacoesCampanha.filter(
        (doacao) => doacao.status === "PENDENTE"
      );
    },
    doacoesConfirmadas() {
      return this.doacoesCampanha.filter(
        (doacao) => doacao.status === "CONFIRMADA"
      );
    },
    doacoesCanceladas() {
      return this.doacoesCampanha.filter(
        (doacao) => doacao.status === "CANCELADA"
      );
    },
    doacoesFiltradas() {
      if (!this.filtroDoacoes) {
        return this.doacoesCampanha;
      }

      const termo = this.filtroDoacoes.toLowerCase();
      return this.doacoesCampanha.filter((doacao) => {
        const doadorNome = doacao.doador
          ? doacao.doador.nome.toLowerCase()
          : "";
        const valor = doacao.valor.toString();
        const status = doacao.status.toLowerCase();
        const formaPagamento = doacao.formaPagamento.toLowerCase();

        return (
          doadorNome.includes(termo) ||
          valor.includes(termo) ||
          status.includes(termo) ||
          formaPagamento.includes(termo)
        );
      });
    },
  },
  created() {
    this.carregarCampanhas();
  },
  methods: {
    async carregarCampanhas() {
      this.loading = true;

      try {
        const response = await axios.get(
          `${process.env.VUE_APP_API_BASE_URL}/api/campanhas`
        );
        this.campanhas = response.data;
        this.campanhasFiltradas = [...this.campanhas];
      } catch (error) {
        console.error("Erro ao carregar campanhas:", error);
        alert("Erro ao carregar campanhas. Por favor, tente novamente.");
      } finally {
        this.loading = false;
      }
    },

    filtrarCampanhas() {
      this.campanhasFiltradas = this.campanhas.filter((campanha) => {
        const termoBusca = this.filtro.termo.toLowerCase();
        const matchTermo =
          !termoBusca ||
          campanha.titulo.toLowerCase().includes(termoBusca) ||
          campanha.descricao.toLowerCase().includes(termoBusca);

        const matchStatus =
          !this.filtro.status || campanha.status === this.filtro.status;

        const matchCategoria =
          !this.filtro.categoria ||
          campanha.categoria === this.filtro.categoria;

        return matchTermo && matchStatus && matchCategoria;
      });
    },

    async verCampanha(campanha) {
      this.campanhaAtual = campanha;
      this.showModalCampanha = true;

      await this.carregarDoacoesCampanha(campanha.id);
    },

    async carregarDoacoesCampanha(campanhaId) {
      this.loadingDoacoes = true;

      try {
        const response = await axios.get(
          `${process.env.VUE_APP_API_BASE_URL}/api/doacoes/campanha/${campanhaId}`
        );
        this.doacoesCampanha = response.data;
      } catch (error) {
        console.error("Erro ao carregar doações:", error);
        this.doacoesCampanha = [];
      } finally {
        this.loadingDoacoes = false;
      }
    },

    fecharModalCampanha() {
      this.showModalCampanha = false;
      this.campanhaAtual = null;
      this.doacoesCampanha = [];
    },

    editarCampanha() {
      alert(`Editar campanha: ${this.campanhaAtual.titulo}`);
    },

    async alterarStatusCampanha() {
      const novoStatus =
        this.campanhaAtual.status === "ATIVA" ? "FINALIZADA" : "ATIVA";
      const mensagem =
        this.campanhaAtual.status === "ATIVA"
          ? `Tem certeza que deseja pausar a campanha "${this.campanhaAtual.titulo}"?`
          : `Tem certeza que deseja ativar a campanha "${this.campanhaAtual.titulo}"?`;

      if (confirm(mensagem)) {
        try {
          const campanhaAtualizada = {
            ...this.campanhaAtual,
            status: novoStatus,
          };

          await axios.put(
            `${process.env.VUE_APP_API_BASE_URL}/api/campanhas/${this.campanhaAtual.id}`,
            campanhaAtualizada
          );

          const index = this.campanhas.findIndex(
            (c) => c.id === this.campanhaAtual.id
          );
          if (index !== -1) {
            this.campanhas[index].status = novoStatus;
            this.campanhaAtual.status = novoStatus;
          }

          const indexFiltrado = this.campanhasFiltradas.findIndex(
            (c) => c.id === this.campanhaAtual.id
          );
          if (indexFiltrado !== -1) {
            this.campanhasFiltradas[indexFiltrado].status = novoStatus;
          }

          alert(
            `Campanha ${
              novoStatus === "ATIVA" ? "ativada" : "pausada"
            } com sucesso!`
          );
        } catch (error) {
          console.error("Erro ao alterar status da campanha:", error);
          alert(
            "Erro ao alterar status da campanha. Por favor, tente novamente."
          );
        }
      }
    },

    async confirmarDoacao(doacao) {
      if (
        confirm(`Confirmar doação de R$ ${this.formatarValor(doacao.valor)}?`)
      ) {
        this.processandoDoacao = doacao.id;

        try {
          await axios.put(
            `${process.env.VUE_APP_API_BASE_URL}/api/doacoes/${doacao.id}/confirmar`
          );

          const index = this.doacoesCampanha.findIndex(
            (d) => d.id === doacao.id
          );
          if (index !== -1) {
            this.doacoesCampanha[index].status = "CONFIRMADA";
          }

          if (this.campanhaAtual) {
            this.campanhaAtual.valorArrecadado += doacao.valor;

            const indexCampanha = this.campanhas.findIndex(
              (c) => c.id === this.campanhaAtual.id
            );
            if (indexCampanha !== -1) {
              this.campanhas[indexCampanha].valorArrecadado =
                this.campanhaAtual.valorArrecadado;
            }

            const indexFiltrado = this.campanhasFiltradas.findIndex(
              (c) => c.id === this.campanhaAtual.id
            );
            if (indexFiltrado !== -1) {
              this.campanhasFiltradas[indexFiltrado].valorArrecadado =
                this.campanhaAtual.valorArrecadado;
            }
          }

          alert("Doação confirmada com sucesso!");
        } catch (error) {
          console.error("Erro ao confirmar doação:", error);
          alert("Erro ao confirmar doação. Por favor, tente novamente.");
        } finally {
          this.processandoDoacao = null;
        }
      }
    },

    async recusarDoacao(doacao) {
      if (
        confirm(`Recusar doação de R$ ${this.formatarValor(doacao.valor)}?`)
      ) {
        this.processandoDoacao = doacao.id;

        try {
          await axios.put(
            `${process.env.VUE_APP_API_BASE_URL}/api/doacoes/${doacao.id}/cancelar`
          );

          const index = this.doacoesCampanha.findIndex(
            (d) => d.id === doacao.id
          );
          if (index !== -1) {
            this.doacoesCampanha[index].status = "CANCELADA";
          }

          if (doacao.status === "CONFIRMADA" && this.campanhaAtual) {
            this.campanhaAtual.valorArrecadado -= doacao.valor;

            const indexCampanha = this.campanhas.findIndex(
              (c) => c.id === this.campanhaAtual.id
            );
            if (indexCampanha !== -1) {
              this.campanhas[indexCampanha].valorArrecadado =
                this.campanhaAtual.valorArrecadado;
            }

            const indexFiltrado = this.campanhasFiltradas.findIndex(
              (c) => c.id === this.campanhaAtual.id
            );
            if (indexFiltrado !== -1) {
              this.campanhasFiltradas[indexFiltrado].valorArrecadado =
                this.campanhaAtual.valorArrecadado;
            }
          }

          alert("Doação recusada com sucesso!");
        } catch (error) {
          console.error("Erro ao recusar doação:", error);
          alert("Erro ao recusar doação. Por favor, tente novamente.");
        } finally {
          this.processandoDoacao = null;
        }
      }
    },

    verDetalhesDoacao(doacao) {
      alert(
        `Detalhes da Doação:\n\nDoador: ${
          doacao.anonimo
            ? "Anônimo"
            : doacao.doador
            ? doacao.doador.nome
            : "N/A"
        }\nValor: R$ ${this.formatarValor(
          doacao.valor
        )}\nData: ${this.formatarData(doacao.dataHora)}\nForma de Pagamento: ${
          doacao.formaPagamento
        }\nStatus: ${doacao.status}\nMensagem: ${
          doacao.mensagem || "Nenhuma mensagem"
        }`
      );
    },

    verAlocacoes() {
      alert(
        `Funcionalidade de visualização de alocações será implementada em breve.`
      );
    },

    calcularProgresso(campanha) {
      if (!campanha.metaFinanceira || campanha.metaFinanceira === 0) {
        return 0;
      }

      const percentual =
        (campanha.valorArrecadado / campanha.metaFinanceira) * 100;
      return Math.min(Math.round(percentual), 100);
    },

    calcularValorMedio() {
      if (this.doacoesConfirmadas.length === 0) {
        return "0,00";
      }

      const total = this.doacoesConfirmadas.reduce(
        (sum, doacao) => sum + doacao.valor,
        0
      );
      const media = total / this.doacoesConfirmadas.length;

      return this.formatarValor(media);
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

    truncateText(text, length) {
      if (!text) return "";
      return text.length > length ? text.substring(0, length) + "..." : text;
    },

    getStatusClass(status) {
      const classes = {
        ATIVA: "badge bg-success",
        FINALIZADA: "badge bg-secondary",
        CANCELADA: "badge bg-danger",
        CONFIRMADA: "badge bg-success",
        PENDENTE: "badge bg-warning text-dark",
      };
      return classes[status] || "badge bg-secondary";
    },
  },
};
</script>

<style scoped>
.admin-campanhas-view {
  background-color: #f8f9fa;
  min-height: 100vh;
}

.card {
  transition: transform 0.2s;
  border: none;
  box-shadow: 0 0.125rem 0.25rem rgba(0, 0, 0, 0.075);
}

.card:hover {
  transform: translateY(-5px);
}

.progress {
  border-radius: 0.5rem;
  height: 8px;
}

.modal {
  transition: all 0.3s ease;
}

.nav-tabs .nav-link {
  color: #495057;
}

.nav-tabs .nav-link.active {
  color: #007bff;
  font-weight: 500;
}
</style>
