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
                v-for="campanha in campanhas.campanhas"
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
                  <!-- Visão Desktop -->
                  <tr
                    v-for="(alocacao, index) in alocacoesFiltradas"
                    :key="`desktop-${index}`"
                    class="d-none d-md-table-row"
                  >
                    <td class="align-middle">
                      {{ getCampanhaNome(alocacao.campanha.id) }}
                    </td>
                    <td class="align-middle">{{ alocacao.tituloAlocacao }}</td>
                    <td class="align-middle">
                      R$ {{ formatarValor(alocacao.valorAlocado) }}
                    </td>
                    <td class="align-middle">
                      {{ formatarData(alocacao.dataAlocacao) }}
                    </td>
                    <td class="align-middle">
                      <button
                        class="btn btn-sm btn-outline-primary"
                        @click="verComprovante(alocacao)"
                      >
                        <i class="fas fa-file-alt"></i> Ver
                      </button>
                    </td>
                  </tr>

                  <!-- Visão Mobile -->
                  <tr
                    v-for="(alocacao, index) in alocacoesFiltradas"
                    :key="`mobile-${index}`"
                    class="d-md-none"
                  >
                    <td colspan="5">
                      <div class="mobile-alocacao card border-0">
                        <div
                          class="d-flex justify-content-between align-items-start"
                        >
                          <div>
                            <h6 class="mb-1">{{ alocacao.tituloAlocacao }}</h6>
                            <p class="mb-1 text-muted small">
                              <strong>Campanha:</strong>
                              {{ getCampanhaNome(alocacao.campanha.id) }}
                            </p>
                            <p class="mb-0 small">
                              <strong>Valor:</strong>
                              R$ {{ formatarValor(alocacao.valorAlocado) }}
                              <span class="mx-2">•</span>
                              <small class="text-muted">{{
                                formatarData(alocacao.dataAlocacao)
                              }}</small>
                            </p>
                          </div>
                          <div class="text-end">
                            <button
                              class="btn btn-sm btn-outline-primary"
                              @click="verComprovante(alocacao)"
                            >
                              <i class="fas fa-file-alt"></i>
                            </button>
                          </div>
                        </div>
                      </div>
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

<script setup>
import { ref, computed, onMounted } from "vue";
import { useStore } from "vuex";
import * as bootstrap from "bootstrap";
import { formatarValor, formatarData } from "../utils";

const store = useStore();
const campanhaId = ref("");
const loading = ref(true);
const alocacaoSelecionada = ref(null);

const transparencia = computed(() => store.state.transparencia || []);
const campanhas = computed(() => store.state.campanhas || { campanhas: [] });

const alocacoesFiltradas = computed(() => {
  if (!transparencia.value) return [];
  return transparencia.value.filter((a) => {
    if (campanhaId.value) return a.campanha.id === parseInt(campanhaId.value);
    return true;
  });
});

const totalAlocado = computed(() =>
  alocacoesFiltradas.value.reduce((t, a) => t + (a.valorAlocado || 0), 0)
);

const getCampanhaNome = (id) => {
  const c = campanhas.value.campanhas.find((c) => c.id === id);
  return c ? c.titulo : "Campanha não encontrada";
};

const verComprovante = (alocacao) => {
  alocacaoSelecionada.value = alocacao;
  const myModal = new bootstrap.Modal(
    document.getElementById("comprovanteModal")
  );
  myModal.show();
};

onMounted(() => {
  Promise.all([
    store.dispatch("fetchCampanhas"),
    store.dispatch("fetchTransparencia"),
  ]).finally(() => {
    loading.value = false;
  });
});
</script>

<style scoped>
.transparencia-view {
  padding-top: 20px;
  padding-bottom: 30px;
}

/* Mobile compact row styling */
.mobile-alocacao {
  background: transparent;
  padding: 8px 0;
}

/* Make table more compact on small screens */
@media (max-width: 767.98px) {
  .table thead {
    display: none;
  }

  .table td {
    border-top: none;
  }

  .mobile-alocacao h6 {
    font-size: 1rem;
    margin-bottom: 0.25rem;
  }

  .mobile-alocacao p.small {
    margin-bottom: 0.25rem;
  }
}
</style>
