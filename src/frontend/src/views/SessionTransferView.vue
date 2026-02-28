<template>
  <div class="container py-5 text-center">
    <div v-if="loading">
      <h3>Validando sessão...</h3>
      <p class="text-muted">Aguarde enquanto realizamos o login.</p>
    </div>

    <div v-else-if="error">
      <h3 class="text-danger">Link inválido ou expirado</h3>
      <router-link to="/login" class="btn btn-primary mt-3">
        Ir para Login
      </router-link>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useStore } from "vuex";

const store = useStore();
const route = useRoute();
const router = useRouter();

const loading = ref(true);
const error = ref(false);

function resolveDestination(destination) {
  if (!destination) return "/";

  try {
    const decoded = decodeURIComponent(destination);

    if (
      decoded.startsWith("/") &&
      !decoded.startsWith("//") &&
      !decoded.includes("http")
    ) {
      return decoded;
    }

    return "/";
  } catch {
    return "/";
  }
}

onMounted(async () => {
  const token = route.params.token;
  const destinationParam = route.query.destination;

  const destination = resolveDestination(destinationParam);

  try {
    if (!store.getters.isLoggedIn) {
      await store.dispatch("sessionExchange", {
        transferToken: token,
      });
    }

    router.replace(destination);
  } catch (err) {
    error.value = true;
  } finally {
    loading.value = false;
  }
});
</script>
