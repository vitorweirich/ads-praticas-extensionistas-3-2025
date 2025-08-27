<template>
  <div class="progress mb-3 progress-with-text" :style="{ height }">
    <div
      class="progress-bar"
      :class="barClass"
      role="progressbar"
      :style="{ width: value + '%' }"
      :aria-valuenow="value"
      aria-valuemin="0"
      aria-valuemax="100"
    ></div>
    <span
      v-if="showText"
      class="progress-text"
      :class="value >= 50 ? 'text-white' : 'text-dark'"
    >
      {{ value }}%
    </span>
  </div>
</template>

<script>
export default {
  name: "ProgressBar",
  props: {
    value: { type: Number, required: true },
    height: { type: String, default: "1rem" },
    barClass: { type: String, default: "" },
    showText: { type: Boolean, default: true },
  },
};
</script>

<style scoped>
.progress-with-text {
  position: relative;
}
.progress-text {
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  font-weight: 600;
  pointer-events: none;
  font-size: 0.9rem;
}
</style>
