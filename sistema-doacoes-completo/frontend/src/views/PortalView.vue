<!-- eslint-disable vue/no-use-v-if-with-v-for -->
<template>
  <div class="portal-view">
    <div class="row mb-4">
      <div class="col-12">
        <h1 class="mb-3">Portal de Campanhas</h1>

        <div class="card mb-4">
          <div class="card-body">
            <div class="row">
              <div class="col-md-8">
                <div class="input-group">
                  <input
                    type="text"
                    class="form-control"
                    placeholder="Buscar campanhas..."
                    v-model="busca"
                  />
                  <button class="btn btn-primary" type="button">
                    <i class="fas fa-search"></i>
                  </button>
                </div>
              </div>
              <div class="col-md-4">
                <select class="form-select" v-model="filtroCategoria">
                  <option value="">Todas as categorias</option>
                  <option value="saude">Saúde</option>
                  <option value="educacao">Educação</option>
                  <option value="emergencia">Emergência</option>
                  <option value="social">Social</option>
                  <option value="ambiental">Ambiental</option>
                </select>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="row">
      <div
        v-if="campanhasFiltradas.length > 0"
        class="col-md-4 mb-4"
        v-for="(campanha, index) in campanhasFiltradas"
        :key="index"
      >
        <div class="card card-campanha h-100">
          <img
            :src="
              campanha.imagemCapa ||
              'https://via.placeholder.com/300x200?text=Campanha'
            "
            class="card-img-top"
            alt="Imagem da campanha"
          />
          <div class="card-body">
            <span class="badge bg-primary mb-2">{{ campanha.categoria }}</span>
            <h5 class="card-title">{{ campanha.titulo }}</h5>
            <p class="card-text">
              {{ campanha.descricao.substring(0, 100) }}...
            </p>
            <div class="progress mb-3">
              <div
                class="progress-bar progress-bar-success"
                role="progressbar"
                :style="{ width: calcularProgresso(campanha) + '%' }"
                :aria-valuenow="calcularProgresso(campanha)"
                aria-valuemin="0"
                aria-valuemax="100"
              >
                {{ calcularProgresso(campanha) }}%
              </div>
            </div>
            <p class="text-muted">
              <small
                >Meta: R$ {{ formatarValor(campanha.metaFinanceira) }} |
                Arrecadado: R$
                {{ formatarValor(campanha.valorArrecadado) }}</small
              >
            </p>
          </div>
          <div class="card-footer bg-white border-top-0">
            <router-link
              :to="'/campanha/' + campanha.id"
              class="btn btn-primary w-100"
              >Saiba mais</router-link
            >
          </div>
        </div>
      </div>
      <div v-else-if="loading" class="col-12 text-center py-5">
        <div class="spinner-border text-primary" role="status">
          <span class="visually-hidden">Carregando...</span>
        </div>
        <p class="mt-2">Carregando campanhas...</p>
      </div>
      <div v-else class="col-12 text-center py-5">
        <p>Nenhuma campanha encontrada com os filtros selecionados.</p>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState } from "vuex";

export default {
  name: "PortalView",
  data() {
    return {
      busca: "",
      filtroCategoria: "",
      loading: true,
    };
  },
  computed: {
    ...mapState(["campanhas"]),
    campanhasFiltradas() {
      return this.campanhas.campanhas
        .filter((campanha) => campanha.status === "ATIVA")
        .filter((campanha) => {
          if (this.busca) {
            const termoBusca = this.busca.toLowerCase();
            return (
              campanha.titulo.toLowerCase().includes(termoBusca) ||
              campanha.descricao.toLowerCase().includes(termoBusca)
            );
          }
          return true;
        })
        .filter((campanha) => {
          if (this.filtroCategoria) {
            return (
              campanha.categoria.toLowerCase() ===
              this.filtroCategoria.toLowerCase()
            );
          }
          return true;
        });
    },
  },
  methods: {
    calcularProgresso(campanha) {
      if (!campanha.metaFinanceira || campanha.metaFinanceira === 0) return 0;
      const progresso =
        (campanha.valorArrecadado / campanha.metaFinanceira) * 100;
      return Math.min(Math.round(progresso), 100);
    },
    formatarValor(valor) {
      return valor
        ? valor.toLocaleString("pt-BR", {
            minimumFractionDigits: 2,
            maximumFractionDigits: 2,
          })
        : "0,00";
    },
  },
  created() {
    this.$store.dispatch("fetchCampanhas").finally(() => {
      this.loading = false;
    });
  },
};
</script>

<style scoped>
.portal-view {
  padding-top: 20px;
  padding-bottom: 30px;
}
</style>
