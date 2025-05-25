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
        <CardCampanha :campanha="campanha" />
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
  components: {
    CardCampanha: () => import("../components/CardCampanha.vue"),
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
