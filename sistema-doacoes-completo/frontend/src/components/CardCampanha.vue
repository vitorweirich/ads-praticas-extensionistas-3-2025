<template>
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
      <p class="card-text">{{ campanha.descricao.substring(0, 100) }}...</p>
      <ProgressBar :value="calcularProgresso(campanha)" />
      <p class="text-muted">
        <small
          >Meta: R$ {{ formatarValor(campanha.metaFinanceira) }} | Arrecadado:
          R$ {{ formatarValor(campanha.valorArrecadado) }}</small
        >
      </p>
    </div>
    <div class="card-footer bg-white border-top-0">
      <router-link
        :to="{ name: 'campanha-detalhe', params: { id: campanha.id } }"
        class="btn btn-primary"
      >
        Saiba mais
      </router-link>
    </div>
  </div>
</template>

<script>
import { calcularProgresso, formatarValor } from "../utils";
import ProgressBar from "./ProgressBar.vue";

export default {
  name: "CardCampanha",
  components: { ProgressBar },
  props: {
    campanha: {
      type: Object,
      required: true,
    },
  },
  methods: {
    calcularProgresso,
    formatarValor,
  },
};
</script>
