<!-- eslint-disable vue/no-use-v-if-with-v-for -->
<template>
  <div class="home">
    <div class="jumbotron bg-light p-5 rounded-3 mb-4">
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

    <div class="row mb-5">
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
      v-if="campanhas.campanhas.length > 0 || campanhas.isLoading"
      class="row"
    >
      <div class="col-12">
        <h2 class="text-center mb-4">Campanhas em Destaque</h2>
      </div>
      <div
        v-if="!campanhas.isLoading"
        class="col-md-4 mb-4"
        v-for="(campanha, index) in campanhasDestaque"
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
      <div v-else class="col-12 text-center">
        <p>Carregando campanhas...</p>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState } from "vuex";

export default {
  name: "HomeView",
  computed: {
    ...mapState(["campanhas"]),
    campanhasDestaque() {
      return this.campanhas.campanhas
        .filter((campanha) => campanha.status === "ATIVA")
        .slice(0, 3);
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
    this.$store.dispatch("fetchCampanhas");
  },
};
</script>
