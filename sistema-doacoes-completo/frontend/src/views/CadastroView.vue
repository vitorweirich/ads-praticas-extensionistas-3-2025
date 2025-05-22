<template>
  <div class="cadastro-view">
    <div class="row justify-content-center">
      <div class="col-md-8">
        <div class="card shadow">
          <div class="card-header bg-primary text-white">
            <h4 class="mb-0">Cadastro de Usuário</h4>
          </div>
          <div class="card-body">
            <div v-if="error" class="alert alert-danger" role="alert">
              {{ error }}
            </div>
            <div v-if="success" class="alert alert-success" role="alert">
              {{ success }}
            </div>
            <form @submit.prevent="handleRegister">
              <div class="row">
                <div class="col-md-6 mb-3">
                  <label for="nome" class="form-label">Nome Completo</label>
                  <input
                    type="text"
                    class="form-control"
                    id="nome"
                    v-model="user.nome"
                    required
                    placeholder="Seu nome completo"
                  />
                </div>
                <div class="col-md-6 mb-3">
                  <label for="email" class="form-label">Email</label>
                  <input
                    type="email"
                    class="form-control"
                    id="email"
                    v-model="user.email"
                    required
                    placeholder="Seu email"
                  />
                </div>
              </div>

              <div class="row">
                <div class="col-md-6 mb-3">
                  <label for="senha" class="form-label">Senha</label>
                  <input
                    type="password"
                    class="form-control"
                    id="senha"
                    v-model="user.senha"
                    required
                    placeholder="Sua senha"
                  />
                  <small class="form-text text-muted"
                    >A senha deve ter pelo menos 6 caracteres</small
                  >
                </div>
                <div class="col-md-6 mb-3">
                  <label for="confirmarSenha" class="form-label"
                    >Confirmar Senha</label
                  >
                  <input
                    type="password"
                    class="form-control"
                    id="confirmarSenha"
                    v-model="confirmarSenha"
                    required
                    placeholder="Confirme sua senha"
                  />
                </div>
              </div>

              <div class="row">
                <div class="col-md-6 mb-3">
                  <label for="telefone" class="form-label"
                    >Telefone (opcional)</label
                  >
                  <input
                    type="tel"
                    class="form-control"
                    id="telefone"
                    v-model="user.telefone"
                    placeholder="Seu telefone"
                  />
                </div>
                <div class="col-md-6 mb-3">
                  <label for="cpfCnpj" class="form-label"
                    >CPF/CNPJ (opcional)</label
                  >
                  <input
                    type="text"
                    class="form-control"
                    id="cpfCnpj"
                    v-model="user.cpfCnpj"
                    placeholder="Seu CPF ou CNPJ"
                  />
                </div>
              </div>

              <div class="mb-3">
                <label for="endereco" class="form-label"
                  >Endereço (opcional)</label
                >
                <textarea
                  class="form-control"
                  id="endereco"
                  v-model="user.endereco"
                  rows="2"
                  placeholder="Seu endereço completo"
                ></textarea>
              </div>

              <div class="d-grid gap-2">
                <button
                  type="submit"
                  class="btn btn-primary"
                  :disabled="loading || !isFormValid"
                >
                  <span
                    v-if="loading"
                    class="spinner-border spinner-border-sm"
                    role="status"
                    aria-hidden="true"
                  ></span>
                  {{ loading ? "Cadastrando..." : "Cadastrar" }}
                </button>
              </div>
            </form>
            <div class="mt-3 text-center">
              <p>
                Já tem uma conta?
                <router-link to="/login">Faça login</router-link>
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "CadastroView",
  data() {
    return {
      user: {
        nome: "",
        email: "",
        senha: "",
        telefone: "",
        cpfCnpj: "",
        endereco: "",
      },
      confirmarSenha: "",
      loading: false,
      error: null,
      success: null,
    };
  },
  computed: {
    isFormValid() {
      return (
        this.user.nome &&
        this.user.email &&
        this.user.senha &&
        this.user.senha.length >= 6 &&
        this.user.senha === this.confirmarSenha
      );
    },
  },
  methods: {
    handleRegister() {
      if (!this.isFormValid) {
        this.error =
          "Por favor, preencha todos os campos obrigatórios corretamente.";
        return;
      }

      if (this.user.senha !== this.confirmarSenha) {
        this.error = "As senhas não coincidem.";
        return;
      }

      this.loading = true;
      this.error = null;
      this.success = null;

      this.$store
        .dispatch("register", this.user)
        .then(() => {
          this.success =
            "Cadastro realizado com sucesso! Você já pode fazer login.";
          this.user = {
            nome: "",
            email: "",
            senha: "",
            telefone: "",
            cpfCnpj: "",
            endereco: "",
          };
          this.confirmarSenha = "";

          setTimeout(() => {
            this.$router.push("/login");
          }, 2000);
        })
        .catch((err) => {
          console.log(err);
          this.error =
            err.response?.data?.message ||
            "Erro ao realizar cadastro. Tente novamente.";
        })
        .finally(() => {
          this.loading = false;
        });
    },
  },
};
</script>

<style scoped>
.cadastro-view {
  padding-top: 30px;
  padding-bottom: 30px;
}
</style>
