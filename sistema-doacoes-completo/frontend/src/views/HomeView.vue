<!-- eslint-disable vue/no-use-v-if-with-v-for -->
<template>
  <div class="home row gap-3">
    <div class="jumbotron bg-light p-sm-5 rounded-3">
      <div class="container">
        <h1 class="display-4">Faça a diferença hoje</h1>
        <p class="lead">
          Apoie campanhas de doação e ajude a transformar vidas em nossa
          comunidade.
        </p>
        <hr class="my-4" />
        <p>
          Conheça nossas campanhas ativas e descubra como você pode contribuir
          para um mundo melhor.
        </p>
        <router-link to="/portal" class="btn btn-primary btn-lg"
          >Ver Campanhas</router-link
        >
      </div>
    </div>

    <div class="row">
      <div class="col-md-4 mb-4">
        <div class="card h-100 text-center">
          <div class="card-body">
            <i class="fas fa-hand-holding-heart fa-3x text-primary mb-3"></i>
            <h3 class="card-title">Doe</h3>
            <p class="card-text">
              Contribua com nossas campanhas e ajude quem mais precisa. Cada
              doação faz a diferença.
            </p>
          </div>
        </div>
      </div>
      <div class="col-md-4 mb-4">
        <div class="card h-100 text-center">
          <div class="card-body">
            <i class="fas fa-search-dollar fa-3x text-primary mb-3"></i>
            <h3 class="card-title">Transparência</h3>
            <p class="card-text">
              Acompanhe como os recursos são utilizados. Nosso compromisso é com
              a transparência total.
            </p>
          </div>
        </div>
      </div>
      <div class="col-md-4 mb-4">
        <div class="card h-100 text-center">
          <div class="card-body">
            <i class="fas fa-users fa-3x text-primary mb-3"></i>
            <h3 class="card-title">Comunidade</h3>
            <p class="card-text">
              Faça parte de uma comunidade que se importa e trabalha por um
              futuro melhor para todos.
            </p>
          </div>
        </div>
      </div>
    </div>

    <div
      class="row"
      v-if="campanhas.campanhas.length > 0 || campanhas.isLoading"
    >
      <div class="col-12">
        <h2 class="text-center mb-4">Campanhas em Destaque</h2>
      </div>

      <template v-if="!campanhas.isLoading">
        <div
          class="col-md-4 mb-4"
          v-for="(campanha, index) in campanhasDestaque"
          :key="index"
        >
          <CardCampanha :campanha="campanha" />
        </div>
      </template>

      <div v-else class="col-12 text-center">
        <p>Carregando campanhas...</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted } from "vue";
import { useStore } from "vuex";
import CardCampanha from "../components/CardCampanha.vue";

const store = useStore();

onMounted(() => {
  store.dispatch("fetchCampanhas");
});

const campanhas = computed(() => store.state.campanhas);

const campanhasDestaque = computed(() => {
  const list = campanhas.value?.campanhas || [];
  return list.filter((c) => c.status === "ATIVA").slice(0, 3);
});
</script>
