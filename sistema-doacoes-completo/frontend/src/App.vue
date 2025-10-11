<template>
  <div id="app">
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
      <div class="container">
        <router-link class="navbar-brand" to="/"
          >Sistema de Doações</router-link
        >
        <button
          id="navbarToggler"
          class="navbar-toggler"
          type="button"
          aria-controls="navbarNav"
          aria-expanded="false"
          aria-label="Toggle navigation"
          @click="toggleNavbar"
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
            <li class="nav-item">
              <router-link class="nav-link" to="/contato">Contato</router-link>
            </li>
            <li class="nav-item" v-if="isAdmin">
              <router-link class="nav-link" to="/admin"
                >Administração</router-link
              >
            </li>
            <li class="nav-item">
              <a class="nav-link" :href="`/README.pdf`" target="_blank"
                >Sobre</a
              >
            </li>
            <li class="nav-item">
              <a
                class="nav-link"
                :href="`/SIMPLIFIED_README.pdf`"
                target="_blank"
                >Sobre (Resumido)</a
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
import { computed, onMounted, onUnmounted } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'
import Collapse from 'bootstrap/js/dist/collapse'

const store = useStore()
const router = useRouter()

let bsCollapseInstance = null
let removeAfterHook = null
let shownListener = null
let hiddenListener = null

onMounted(() => {
  store.dispatch('checkToken')

  const collapseEl = document.getElementById('navbarNav')
  if (collapseEl) {
    bsCollapseInstance = new Collapse(collapseEl, { toggle: false })

    removeAfterHook = router.afterEach(() => {
      if (collapseEl.classList.contains('show') && bsCollapseInstance) {
        bsCollapseInstance.hide()
      }
    })

    collapseEl.addEventListener('click', (e) => {
      const target = e.target.closest('.nav-link')
      if (target && collapseEl.classList.contains('show') && bsCollapseInstance) {
        bsCollapseInstance.hide()
      }
    })

    const toggler = document.getElementById('navbarToggler')
    if (toggler) {
      shownListener = () => toggler.setAttribute('aria-expanded', 'true')
      hiddenListener = () => toggler.setAttribute('aria-expanded', 'false')
      collapseEl.addEventListener('shown.bs.collapse', shownListener)
      collapseEl.addEventListener('hidden.bs.collapse', hiddenListener)
    }
  }
})

onUnmounted(() => {
  if (removeAfterHook) removeAfterHook()
  if (bsCollapseInstance) {
    try { bsCollapseInstance.dispose() } catch (e) { /* ignore */ }
    bsCollapseInstance = null
  }
  const collapseEl = document.getElementById('navbarNav')
  if (collapseEl) {
    if (shownListener) collapseEl.removeEventListener('shown.bs.collapse', shownListener)
    if (hiddenListener) collapseEl.removeEventListener('hidden.bs.collapse', hiddenListener)
  }
})

const isLoggedIn = computed(() => store.getters.isLoggedIn)
const currentUser = computed(() => store.getters.currentUser)
const isAdmin = computed(() => store.getters.isAdmin)

function logout() {
  store.dispatch('logout').then(() => {
    router.push('/login')
  })
}

function toggleNavbar() {
  const collapseEl = document.getElementById('navbarNav')
  if (!collapseEl) return
  // Toggle using the Collapse instance
  if (collapseEl.classList.contains('show')) {
    bsCollapseInstance && bsCollapseInstance.hide()
  } else {
    bsCollapseInstance && bsCollapseInstance.show()
  }
}
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
