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

export default {
  name: "CardCampanha",
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
