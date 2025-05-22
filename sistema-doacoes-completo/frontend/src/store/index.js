import Vue from 'vue'
import Vuex from 'vuex'
import axios from 'axios'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    status: '',
    token: localStorage.getItem('token') || '',
    user: JSON.parse(localStorage.getItem('user')) || {},
    campanhas: [],
    transparencia: []
  },
  getters: {
    isLoggedIn: state => !!state.token,
    authStatus: state => state.status,
    currentUser: state => state.user,
    isAdmin: state => {
      return state.user && state.user.roles && state.user.roles.includes('ROLE_ADMINISTRADOR')
    },
    campanhasAtivas: state => {
      return state.campanhas.filter(campanha => campanha.status === 'ATIVA')
    }
  },
  mutations: {
    auth_request(state) {
      state.status = 'loading'
    },
    auth_success(state, { token, user }) {
      state.status = 'success'
      state.token = token
      state.user = user
    },
    auth_error(state) {
      state.status = 'error'
    },
    logout(state) {
      state.status = ''
      state.token = ''
      state.user = {}
    },
    set_campanhas(state, campanhas) {
      state.campanhas = campanhas
    },
    set_transparencia(state, transparencia) {
      state.transparencia = transparencia
    }
  },
  actions: {
    login({ commit }, user) {
      return new Promise((resolve, reject) => {
        commit('auth_request')
        axios.post('http://localhost:8080/api/auth/login', user)
          .then(resp => {
            const token = resp.data.accessToken
            const user = {
              id: resp.data.id,
              nome: resp.data.nome,
              email: resp.data.email,
              roles: resp.data.roles
            }
            localStorage.setItem('token', token)
            localStorage.setItem('user', JSON.stringify(user))
            axios.defaults.headers.common['Authorization'] = 'Bearer ' + token
            commit('auth_success', { token, user })
            resolve(resp)
          })
          .catch(err => {
            commit('auth_error')
            localStorage.removeItem('token')
            localStorage.removeItem('user')
            reject(err)
          })
      })
    },
    register({ commit }, user) {
      return new Promise((resolve, reject) => {
        commit('auth_request')
        axios.post('http://localhost:8080/api/auth/cadastro', user)
          .then(resp => {
            resolve(resp)
          })
          .catch(err => {
            commit('auth_error')
            reject(err)
          })
      })
    },
    logout({ commit }) {
      return new Promise((resolve) => {
        commit('logout')
        localStorage.removeItem('token')
        localStorage.removeItem('user')
        delete axios.defaults.headers.common['Authorization']
        resolve()
      })
    },
    fetchCampanhas({ commit }) {
      return new Promise((resolve, reject) => {
        axios.get('http://localhost:8080/api/campanhas/publicas')
          .then(resp => {
            commit('set_campanhas', resp.data)
            resolve(resp)
          })
          .catch(err => {
            reject(err)
          })
      })
    },
    fetchTransparencia({ commit }) {
      return new Promise((resolve, reject) => {
        axios.get('http://localhost:8080/api/transparencia/publica')
          .then(resp => {
            commit('set_transparencia', resp.data)
            resolve(resp)
          })
          .catch(err => {
            reject(err)
          })
      })
    }
  },
  modules: {
  }
})
