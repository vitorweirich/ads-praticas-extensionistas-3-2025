<template>
  <div class="campanha-detalhe">
    <div class="container py-5">
      <div v-if="loading" class="text-center py-5">
        <div class="spinner-border text-primary" role="status">
          <span class="visually-hidden">Carregando...</span>
        </div>
        <p class="mt-3">Carregando detalhes da campanha...</p>
      </div>

      <div v-else-if="error" class="alert alert-danger" role="alert">
        <h4 class="alert-heading">Erro ao carregar campanha</h4>
        <p>{{ error }}</p>
        <hr />
        <div class="d-flex justify-content-end">
          <router-link to="/portal" class="btn btn-outline-danger">
            Voltar para o Portal
          </router-link>
        </div>
      </div>

      <div v-else>
        <div class="row mb-5">
          <div class="col-lg-8">
            <div class="card border-0 shadow-sm overflow-hidden mb-4">
              <img
                :src="
                  campanha.imagemCapa ||
                  'https://via.placeholder.com/800x400?text=Imagem+da+Campanha'
                "
                class="card-img-top"
                alt="Imagem da campanha"
              />
              <div class="card-body p-4">
                <div
                  class="d-flex justify-content-between align-items-center mb-2"
                >
                  <span class="badge bg-primary">{{ campanha.categoria }}</span>
                  <span :class="getStatusClass(campanha.status)">{{
                    campanha.status
                  }}</span>
                </div>
                <h1 class="card-title mb-3">{{ campanha.titulo }}</h1>
                <p class="card-text text-muted">
                  <i class="fas fa-calendar-alt me-2"></i>
                  Término: {{ formatarData(campanha.dataTermino) }}
                </p>
                <div class="progress mb-3" style="height: 10px">
                  <div
                    class="progress-bar bg-success"
                    role="progressbar"
                    :style="{ width: progressoPercentual + '%' }"
                    :aria-valuenow="progressoPercentual"
                    aria-valuemin="0"
                    aria-valuemax="100"
                  ></div>
                </div>
                <div class="d-flex justify-content-between mb-4">
                  <div>
                    <strong>Arrecadado:</strong> R$
                    {{ formatarValor(campanha.valorArrecadado) }}
                  </div>
                  <div>
                    <strong>Meta:</strong> R$
                    {{ formatarValor(campanha.metaFinanceira) }}
                  </div>
                </div>
                <div class="card-text mb-4" v-html="campanha.descricao"></div>
                <div class="d-flex justify-content-between align-items-center">
                  <router-link to="/portal" class="btn btn-outline-secondary">
                    <i class="fas fa-arrow-left me-2"></i> Voltar
                  </router-link>
                  <button
                    class="btn btn-primary"
                    @click="showDoacaoModal = true"
                    :disabled="campanha.status !== 'ATIVA'"
                  >
                    <i class="fas fa-hand-holding-heart me-2"></i> Fazer Doação
                  </button>
                </div>
              </div>
            </div>

            <div class="card border-0 shadow-sm mb-4">
              <div class="card-header bg-white">
                <h4 class="mb-0">
                  <i class="fas fa-users me-2"></i> Beneficiários
                </h4>
              </div>
              <div class="card-body p-4">
                <p>
                  {{
                    campanha.beneficiarios ||
                    "Informações sobre beneficiários não disponíveis."
                  }}
                </p>
              </div>
            </div>

            <div
              v-if="
                campanha.galeriaImagens && campanha.galeriaImagens.length > 0
              "
              class="card border-0 shadow-sm mb-4"
            >
              <div class="card-header bg-white">
                <h4 class="mb-0"><i class="fas fa-images me-2"></i> Galeria</h4>
              </div>
              <div class="card-body p-4">
                <div class="row g-3">
                  <div
                    v-for="(imagem, index) in campanha.galeriaImagens"
                    :key="index"
                    class="col-md-4 col-6"
                  >
                    <img
                      :src="imagem"
                      class="img-fluid rounded"
                      :alt="`Imagem ${index + 1} da campanha`"
                    />
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div class="col-lg-4">
            <div class="card border-0 shadow-sm mb-4">
              <div class="card-header bg-white">
                <h4 class="mb-0">
                  <i class="fas fa-chart-pie me-2"></i> Estatísticas
                </h4>
              </div>
              <div class="card-body p-4">
                <div class="d-flex justify-content-between mb-3">
                  <span>Progresso:</span>
                  <span class="fw-bold">{{ progressoPercentual }}%</span>
                </div>
                <div class="d-flex justify-content-between mb-3">
                  <span>Doações recebidas:</span>
                  <span class="fw-bold">{{ campanha.totalDoacoes || 0 }}</span>
                </div>
                <div class="d-flex justify-content-between mb-3">
                  <span>Dias restantes:</span>
                  <span class="fw-bold">{{ diasRestantes }}</span>
                </div>
              </div>
            </div>

            <div class="card border-0 shadow-sm mb-4">
              <div class="card-header bg-white">
                <h4 class="mb-0">
                  <i class="fas fa-share-alt me-2"></i> Compartilhar
                </h4>
              </div>
              <div class="card-body p-4">
                <div class="d-flex justify-content-around">
                  <a
                    href="#"
                    class="btn btn-outline-primary"
                    @click.prevent="compartilhar('facebook')"
                  >
                    <i class="fab fa-facebook-f"></i>
                  </a>
                  <a
                    href="#"
                    class="btn btn-outline-info"
                    @click.prevent="compartilhar('twitter')"
                  >
                    <i class="fab fa-twitter"></i>
                  </a>
                  <a
                    href="#"
                    class="btn btn-outline-success"
                    @click.prevent="compartilhar('whatsapp')"
                  >
                    <i class="fab fa-whatsapp"></i>
                  </a>
                  <a
                    href="#"
                    class="btn btn-outline-secondary"
                    @click.prevent="compartilhar('email')"
                  >
                    <i class="fas fa-envelope"></i>
                  </a>
                </div>
              </div>
            </div>

            <div class="card border-0 shadow-sm">
              <div class="card-header bg-white">
                <h4 class="mb-0">
                  <i class="fas fa-donate me-2"></i> Doações Recentes
                </h4>
              </div>
              <div class="card-body p-0">
                <ul class="list-group list-group-flush">
                  <li
                    v-if="doacoesRecentes.length === 0"
                    class="list-group-item py-3"
                  >
                    <p class="text-center text-muted mb-0">
                      Nenhuma doação registrada ainda.
                    </p>
                  </li>
                  <li
                    v-for="(doacao, index) in doacoesRecentes"
                    :key="index"
                    class="list-group-item py-3"
                  >
                    <div
                      class="d-flex justify-content-between align-items-center"
                    >
                      <div>
                        <h6 class="mb-0">
                          {{ doacao.anonimo ? "Anônimo" : doacao.doador.nome }}
                        </h6>
                        <small class="text-muted">{{
                          formatarData(doacao.dataHora)
                        }}</small>
                      </div>
                      <span class="badge bg-success"
                        >R$ {{ formatarValor(doacao.valor) }}</span
                      >
                    </div>
                  </li>
                </ul>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div
      class="modal fade"
      :class="{ 'show d-block': showDoacaoModal }"
      tabindex="-1"
      :style="{ background: showDoacaoModal ? 'rgba(0,0,0,0.5)' : '' }"
    >
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">Fazer Doação</h5>
            <button
              type="button"
              class="btn-close"
              @click="showDoacaoModal = false"
            ></button>
          </div>
          <div class="modal-body">
            <form @submit.prevent="fazerDoacao">
              <div class="mb-3">
                <label for="valorDoacao" class="form-label"
                  >Valor da Doação (R$)</label
                >
                <input
                  type="number"
                  class="form-control"
                  id="valorDoacao"
                  v-model="doacao.valor"
                  min="5"
                  step="5"
                  required
                />
                <div class="form-text">Valor mínimo: R$ 5,00</div>
              </div>

              <div class="mb-3">
                <div class="form-check">
                  <input
                    class="form-check-input"
                    type="checkbox"
                    id="doacaoAnonima"
                    v-model="doacao.anonimo"
                  />
                  <label class="form-check-label" for="doacaoAnonima">
                    Fazer doação anônima
                  </label>
                </div>
              </div>

              <div class="mb-3">
                <label for="formaPagamento" class="form-label"
                  >Forma de Pagamento</label
                >
                <select
                  class="form-select"
                  id="formaPagamento"
                  v-model="doacao.formaPagamento"
                  required
                >
                  <option value="">Selecione uma forma de pagamento</option>
                  <option value="PIX">PIX</option>
                  <option value="CARTAO_CREDITO">Cartão de Crédito</option>
                  <option value="BOLETO">Boleto Bancário</option>
                </select>
              </div>

              <div class="mb-3">
                <label for="mensagem" class="form-label"
                  >Mensagem (opcional)</label
                >
                <textarea
                  class="form-control"
                  id="mensagem"
                  v-model="doacao.mensagem"
                  rows="3"
                ></textarea>
              </div>

              <div class="alert alert-info" role="alert">
                <i class="fas fa-info-circle me-2"></i>
                Sua doação será processada de forma segura. Após a confirmação,
                você receberá um comprovante.
              </div>
            </form>
          </div>
          <div class="modal-footer">
            <button
              type="button"
              class="btn btn-secondary"
              @click="showDoacaoModal = false"
            >
              Cancelar
            </button>
            <button
              type="button"
              class="btn btn-primary"
              @click="fazerDoacao"
              :disabled="doacaoLoading"
            >
              <span
                v-if="doacaoLoading"
                class="spinner-border spinner-border-sm me-2"
                role="status"
              ></span>
              Confirmar Doação
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { formatarValor, formatarData } from "../utils";

export default {
  name: "CampanhaDetalheView",
  data() {
    return {
      campanhaId: null,
      campanha: {},
      doacoesRecentes: [],
      loading: true,
      error: null,
      showDoacaoModal: false,
      doacao: {
        valor: 50,
        anonimo: false,
        formaPagamento: "",
        mensagem: "",
      },
      doacaoLoading: false,
    };
  },
  computed: {
    progressoPercentual() {
      if (!this.campanha.metaFinanceira || this.campanha.metaFinanceira === 0) {
        return 0;
      }
      const percentual =
        (this.campanha.valorArrecadado / this.campanha.metaFinanceira) * 100;
      return Math.min(Math.round(percentual), 100);
    },
    diasRestantes() {
      if (!this.campanha.dataTermino) {
        return "N/A";
      }

      const hoje = new Date();
      const dataTermino = new Date(this.campanha.dataTermino);
      const diffTime = dataTermino - hoje;
      const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));

      if (diffDays < 0) {
        return "Encerrada";
      }
      return diffDays;
    },
  },
  created() {
    this.campanhaId = this.$route.params.id;
    this.carregarCampanha();
    this.carregarDoacoesRecentes();
  },
  methods: {
    carregarCampanha() {
      this.loading = true;
      this.error = null;

      axios
        .get(
          `${process.env.VUE_APP_API_BASE_URL}/api/campanhas/${this.campanhaId}`
        )
        .then((response) => {
          this.campanha = response.data;
          this.loading = false;
        })
        .catch((error) => {
          console.error("Erro ao carregar campanha:", error);
          this.error =
            "Não foi possível carregar os detalhes da campanha. Por favor, tente novamente mais tarde.";
          this.loading = false;
        });
    },
    carregarDoacoesRecentes() {
      axios
        .get(
          `${process.env.VUE_APP_API_BASE_URL}/api/doacoes/campanha/${this.campanhaId}`
        )
        .then((response) => {
          // O backend já retorna as doações ordenadas e limitadas
          this.doacoesRecentes = response.data;
        })
        .catch(() => {
          // Show empty donations list in case of error
          this.doacoesRecentes = [];
        });
    },
    formatarValor,
    formatarData,
    getStatusClass(status) {
      const classes = {
        ATIVA: "badge bg-success",
        FINALIZADA: "badge bg-secondary",
        CANCELADA: "badge bg-danger",
      };
      return classes[status] || "badge bg-secondary";
    },
    compartilhar(rede) {
      const url = window.location.href;
      const titulo = `Ajude na campanha: ${this.campanha.titulo}`;

      let shareUrl = "";

      switch (rede) {
        case "facebook":
          shareUrl = `https://www.facebook.com/sharer/sharer.php?u=${encodeURIComponent(
            url
          )}`;
          break;
        case "twitter":
          shareUrl = `https://twitter.com/intent/tweet?text=${encodeURIComponent(
            titulo
          )}&url=${encodeURIComponent(url)}`;
          break;
        case "whatsapp":
          shareUrl = `https://api.whatsapp.com/send?text=${encodeURIComponent(
            titulo + " " + url
          )}`;
          break;
        case "email":
          shareUrl = `mailto:?subject=${encodeURIComponent(
            titulo
          )}&body=${encodeURIComponent("Confira esta campanha: " + url)}`;
          break;
      }

      if (shareUrl) {
        window.open(shareUrl, "_blank");
      }
    },
    fazerDoacao() {
      if (!this.doacao.valor || !this.doacao.formaPagamento) {
        alert("Por favor, preencha todos os campos obrigatórios.");
        return;
      }

      this.doacaoLoading = true;

      const doacaoObj = {
        campanha: { id: this.campanhaId },
        valor: this.doacao.valor,
        anonimo: this.doacao.anonimo,
        formaPagamento: this.doacao.formaPagamento,
        mensagem: this.doacao.mensagem,
        status: "PENDENTE",
      };

      if (!this.doacao.anonimo && this.$store.getters.isLoggedIn) {
        doacaoObj.doador = { id: this.$store.getters.currentUser.id };
      }

      axios
        .post(`${process.env.VUE_APP_API_BASE_URL}/api/doacoes`, doacaoObj)
        .then(() => {
          this.doacaoLoading = false;
          this.showDoacaoModal = false;

          this.doacao = {
            valor: 50,
            anonimo: false,
            formaPagamento: "",
            mensagem: "",
          };

          alert(
            "Doação realizada com sucesso! Obrigado pela sua contribuição."
          );

          this.carregarCampanha();
          this.carregarDoacoesRecentes();
        })
        .catch((error) => {
          console.error("Erro ao processar doação:", error);
          this.doacaoLoading = false;
          alert(
            "Ocorreu um erro ao processar sua doação. Por favor, tente novamente."
          );
        });
    },
  },
};
</script>

<style scoped>
.campanha-detalhe {
  background-color: #f8f9fa;
  min-height: 100vh;
}

.modal {
  transition: all 0.3s ease;
}

.progress {
  border-radius: 0.5rem;
}

.progress-bar {
  transition: width 1s ease;
}

.card {
  border-radius: 0.5rem;
  transition: transform 0.3s ease;
}

.card:hover {
  transform: translateY(-5px);
}

.btn-outline-primary:hover,
.btn-outline-info:hover,
.btn-outline-success:hover,
.btn-outline-secondary:hover {
  transform: scale(1.1);
}
</style>
