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
      <template v-if="campanhasFiltradas.length > 0">
        <div
          class="col-md-4 mb-4"
          v-for="(campanha, index) in campanhasFiltradas"
          :key="index"
        >
          <CardCampanha :campanha="campanha" />
        </div>
      </template>

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

<script setup>
import { ref, computed, onMounted } from "vue";
import { useStore } from "vuex";
import CardCampanha from "../components/CardCampanha.vue";

const store = useStore();
const busca = ref("");
const filtroCategoria = ref("");
const loading = ref(true);

onMounted(() => {
  store.dispatch("fetchCampanhas").finally(() => (loading.value = false));
});

const campanhas = computed(() => store.state.campanhas);

const campanhasFiltradas = computed(() => {
  const list = campanhas.value?.campanhas || [];
  return list
    .filter((c) => c.status === "ATIVA")
    .filter((c) => {
      if (busca.value) {
        const termo = busca.value.toLowerCase();
        return (
          c.titulo.toLowerCase().includes(termo) ||
          c.descricao.toLowerCase().includes(termo)
        );
      }
      return true;
    })
    .filter((c) => {
      if (filtroCategoria.value) {
        return (
          c.categoria.toLowerCase() === filtroCategoria.value.toLowerCase()
        );
      }
      return true;
    });
});
</script>

<style scoped>
.portal-view {
  padding-top: 20px;
  padding-bottom: 30px;
}
</style>
