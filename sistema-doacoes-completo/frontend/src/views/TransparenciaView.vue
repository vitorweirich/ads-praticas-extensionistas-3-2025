<template>
  <div class="transparencia-view">
    <div class="row mb-4">
      <div class="col-12">
        <h1 class="mb-3">Transparência</h1>
        <p class="lead">
          Acompanhe como os recursos das doações estão sendo utilizados em
          nossas campanhas.
        </p>
      </div>
    </div>

    <div class="row mb-4">
      <div class="col-md-4 mb-3">
        <div class="card">
          <div class="card-body">
            <h5 class="card-title">Selecione uma campanha</h5>
            <select class="form-select" v-model="campanhaId">
              <option value="">Todas as campanhas</option>
              <option
                v-for="campanha in campanhas"
                :key="campanha.id"
                :value="campanha.id"
              >
                {{ campanha.titulo }}
              </option>
            </select>
          </div>
        </div>
      </div>
      <div class="col-md-8 mb-3">
        <div class="card bg-light">
          <div class="card-body">
            <div class="row align-items-center">
              <div class="col-md-8">
                <h5 class="card-title">Resumo de Transparência</h5>
                <p class="mb-0">
                  Total de recursos alocados:
                  <strong>R$ {{ formatarValor(totalAlocado) }}</strong>
                </p>
                <p class="mb-0">
                  Número de alocações:
                  <strong>{{ alocacoesFiltradas.length }}</strong>
                </p>
              </div>
              <div class="col-md-4 text-end">
                <i class="fas fa-search-dollar fa-3x text-primary"></i>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="row">
      <div class="col-12">
        <div class="card">
          <div class="card-header bg-primary text-white">
            <h5 class="mb-0">Alocações de Recursos</h5>
          </div>
          <div class="card-body p-0">
            <div class="table-responsive">
              <table class="table table-hover mb-0">
                <thead>
                  <tr>
                    <th>Campanha</th>
                    <th>Título</th>
                    <th>Valor Alocado</th>
                    <th>Data</th>
                    <th>Comprovante</th>
                  </tr>
                </thead>
                <tbody>
                  <tr
                    v-for="(alocacao, index) in alocacoesFiltradas"
                    :key="index"
                  >
                    <td>{{ getCampanhaNome(alocacao.campanha.id) }}</td>
                    <td>{{ alocacao.tituloAlocacao }}</td>
                    <td>R$ {{ formatarValor(alocacao.valorAlocado) }}</td>
                    <td>{{ formatarData(alocacao.dataAlocacao) }}</td>
                    <td>
                      <button
                        class="btn btn-sm btn-outline-primary"
                        @click="verComprovante(alocacao)"
                      >
                        <i class="fas fa-file-alt"></i> Ver
                      </button>
                    </td>
                  </tr>
                  <tr v-if="alocacoesFiltradas.length === 0">
                    <td colspan="5" class="text-center py-3">
                      <p v-if="loading" class="mb-0">
                        <span
                          class="spinner-border spinner-border-sm"
                          role="status"
                          aria-hidden="true"
                        ></span>
                        Carregando dados de transparência...
                      </p>
                      <p v-else class="mb-0">
                        Nenhuma alocação de recursos encontrada.
                      </p>
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
      id="comprovanteModal"
      tabindex="-1"
      aria-labelledby="comprovanteModalLabel"
      aria-hidden="true"
    >
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="comprovanteModalLabel">
              Comprovante de Alocação
            </h5>
            <button
              type="button"
              class="btn-close"
              data-bs-dismiss="modal"
              aria-label="Close"
            ></button>
          </div>
          <div class="modal-body">
            <div v-if="alocacaoSelecionada">
              <h6>{{ alocacaoSelecionada.tituloAlocacao }}</h6>
              <p>
                <strong>Campanha:</strong>
                {{ getCampanhaNome(alocacaoSelecionada.campanha.id) }}
              </p>
              <p>
                <strong>Valor:</strong> R$
                {{ formatarValor(alocacaoSelecionada.valorAlocado) }}
              </p>
              <p>
                <strong>Data:</strong>
                {{ formatarData(alocacaoSelecionada.dataAlocacao) }}
              </p>
              <p>
                <strong>Descrição:</strong>
                {{ alocacaoSelecionada.descricaoAlocacao }}
              </p>
              <div class="text-center mt-3">
                <img
                  :src="
                    alocacaoSelecionada.comprovante ||
                    'https://via.placeholder.com/600x400?text=Comprovante'
                  "
                  class="img-fluid border"
                  alt="Comprovante"
                />
              </div>
            </div>
          </div>
          <div class="modal-footer">
            <button
              type="button"
              class="btn btn-secondary"
              data-bs-dismiss="modal"
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
import { mapState } from "vuex";
import * as bootstrap from "bootstrap";
import { formatarValor, formatarData } from "../utils";

export default {
  name: "TransparenciaView",
  data() {
    return {
      campanhaId: "",
      loading: true,
      alocacaoSelecionada: null,
    };
  },
  computed: {
    ...mapState(["transparencia", "campanhas"]),
    alocacoesFiltradas() {
      if (!this.transparencia) return [];

      return this.transparencia.filter((alocacao) => {
        if (this.campanhaId) {
          return alocacao.campanha.id === parseInt(this.campanhaId);
        }
        return true;
      });
    },
    totalAlocado() {
      return this.alocacoesFiltradas.reduce((total, alocacao) => {
        return total + (alocacao.valorAlocado || 0);
      }, 0);
    },
  },
  methods: {
    formatarValor,
    formatarData,
    getCampanhaNome(id) {
      const campanha = this.campanhas.campanhas.find((c) => c.id === id);
      return campanha ? campanha.titulo : "Campanha não encontrada";
    },
    verComprovante(alocacao) {
      this.alocacaoSelecionada = alocacao;
      const myModal = new bootstrap.Modal(
        document.getElementById("comprovanteModal")
      );
      myModal.show();
    },
  },
  created() {
    Promise.all([
      this.$store.dispatch("fetchCampanhas"),
      this.$store.dispatch("fetchTransparencia"),
    ]).finally(() => {
      this.loading = false;
    });
  },
};
</script>

<style scoped>
.transparencia-view {
  padding-top: 20px;
  padding-bottom: 30px;
}
</style>
