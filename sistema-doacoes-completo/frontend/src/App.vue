<template>
  <div id="app">
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
      <div class="container">
        <router-link class="navbar-brand" to="/"
          >Sistema de Doações</router-link
        >
        <button
          class="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navbarNav"
          aria-controls="navbarNav"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
          <ul class="navbar-nav me-auto">
            <li class="nav-item">
              <router-link class="nav-link" to="/portal">Campanhas</router-link>
            </li>
            <li class="nav-item">
              <router-link class="nav-link" to="/transparencia"
                >Transparência</router-link
              >
            </li>
            <li class="nav-item" v-if="isAdmin">
              <router-link class="nav-link" to="/admin"
                >Administração</router-link
              >
            </li>
          </ul>
          <ul class="navbar-nav">
            <template v-if="!isLoggedIn">
              <li class="nav-item">
                <router-link class="nav-link" to="/login">Login</router-link>
              </li>
              <li class="nav-item">
                <router-link class="nav-link" to="/cadastro"
                  >Cadastre-se</router-link
                >
              </li>
            </template>
            <template v-else>
              <li class="nav-item dropdown">
                <a
                  class="nav-link dropdown-toggle"
                  href="#"
                  id="navbarDropdown"
                  role="button"
                  data-bs-toggle="dropdown"
                  aria-expanded="false"
                >
                  {{ currentUser.nome }}
                </a>
                <ul
                  class="dropdown-menu dropdown-menu-end"
                  aria-labelledby="navbarDropdown"
                >
                  <li>
                    <router-link class="dropdown-item" to="/perfil"
                      >Meu Perfil</router-link
                    >
                  </li>
                  <li><hr class="dropdown-divider" /></li>
                  <li>
                    <a class="dropdown-item" href="#" @click.prevent="logout"
                      >Sair</a
                    >
                  </li>
                </ul>
              </li>
            </template>
          </ul>
        </div>
      </div>
    </nav>

    <main class="container py-4">
      <router-view />
    </main>

    <footer class="bg-light py-4 mt-5">
      <div class="container text-center">
        <p class="mb-0">
          &copy; 2025 Sistema de Doações. Todos os direitos reservados.
        </p>
      </div>
    </footer>
  </div>
</template>

<script setup lang="js">
import { mapGetters } from "vuex";

export default {
  name: "App",
  created() {
    this.$store.dispatch('checkToken');
  },
  computed: {
    ...mapGetters(["isLoggedIn", "currentUser", "isAdmin"]),
  },
  methods: {
    logout() {
      this.$store.dispatch("logout").then(() => {
        this.$router.push("/login");
      });
    },
  },
};
</script>

<style>
#app {
  font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

main {
  flex: 1;
}

.card-campanha {
  transition: transform 0.3s;
}

.card-campanha:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
}

.progress-bar-success {
  background-color: #28a745;
}
</style>
