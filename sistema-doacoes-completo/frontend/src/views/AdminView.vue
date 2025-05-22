<template>
  <div class="admin-view">
    <div class="row mb-4">
      <div class="col-12">
        <h1 class="mb-3">Painel Administrativo</h1>
      </div>
    </div>

    <div class="row mb-4">
      <div class="col-md-3 mb-4">
        <div class="card bg-primary text-white">
          <div class="card-body">
            <div class="d-flex justify-content-between align-items-center">
              <div>
                <h6 class="card-title">Campanhas Ativas</h6>
                <h2 class="mb-0">{{ estatisticas.campanhasAtivas }}</h2>
              </div>
              <i class="fas fa-bullhorn fa-2x"></i>
            </div>
          </div>
        </div>
      </div>
      <div class="col-md-3 mb-4">
        <div class="card bg-success text-white">
          <div class="card-body">
            <div class="d-flex justify-content-between align-items-center">
              <div>
                <h6 class="card-title">Total Arrecadado</h6>
                <h2 class="mb-0">
                  R$ {{ formatarValor(estatisticas.totalArrecadado) }}
                </h2>
              </div>
              <i class="fas fa-hand-holding-usd fa-2x"></i>
            </div>
          </div>
        </div>
      </div>
      <div class="col-md-3 mb-4">
        <div class="card bg-info text-white">
          <div class="card-body">
            <div class="d-flex justify-content-between align-items-center">
              <div>
                <h6 class="card-title">Doações Recebidas</h6>
                <h2 class="mb-0">{{ estatisticas.totalDoacoes }}</h2>
              </div>
              <i class="fas fa-donate fa-2x"></i>
            </div>
          </div>
        </div>
      </div>
      <div class="col-md-3 mb-4">
        <div class="card bg-warning text-dark">
          <div class="card-body">
            <div class="d-flex justify-content-between align-items-center">
              <div>
                <h6 class="card-title">Usuários Cadastrados</h6>
                <h2 class="mb-0">{{ estatisticas.totalUsuarios }}</h2>
              </div>
              <i class="fas fa-users fa-2x"></i>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="row">
      <div class="col-12">
        <ul class="nav nav-tabs" id="adminTab" role="tablist">
          <li class="nav-item" role="presentation">
            <button
              class="nav-link active"
              id="campanhas-tab"
              data-bs-toggle="tab"
              data-bs-target="#campanhas"
              type="button"
              role="tab"
              aria-controls="campanhas"
              aria-selected="true"
            >
              Campanhas
            </button>
          </li>
          <li class="nav-item" role="presentation">
            <button
              class="nav-link"
              id="doacoes-tab"
              data-bs-toggle="tab"
              data-bs-target="#doacoes"
              type="button"
              role="tab"
              aria-controls="doacoes"
              aria-selected="false"
            >
              Doações
            </button>
          </li>
          <li class="nav-item" role="presentation">
            <button
              class="nav-link"
              id="transparencia-tab"
              data-bs-toggle="tab"
              data-bs-target="#transparencia"
              type="button"
              role="tab"
              aria-controls="transparencia"
              aria-selected="false"
            >
              Transparência
            </button>
          </li>
          <li class="nav-item" role="presentation">
            <button
              class="nav-link"
              id="usuarios-tab"
              data-bs-toggle="tab"
              data-bs-target="#usuarios"
              type="button"
              role="tab"
              aria-controls="usuarios"
              aria-selected="false"
            >
              Usuários
            </button>
          </li>
        </ul>
        <div class="tab-content" id="adminTabContent">
          <!-- Aba de Campanhas -->
          <div
            class="tab-pane fade show active"
            id="campanhas"
            role="tabpanel"
            aria-labelledby="campanhas-tab"
          >
            <div class="card border-top-0 rounded-top-0">
              <div class="card-body">
                <div
                  class="d-flex justify-content-between align-items-center mb-3"
                >
                  <h5 class="card-title">Gerenciar Campanhas</h5>
                  <button class="btn btn-primary" @click="novaCampanha">
                    <i class="fas fa-plus"></i> Nova Campanha
                  </button>
                </div>
                <div class="table-responsive">
                  <table class="table table-hover">
                    <thead>
                      <tr>
                        <th>Título</th>
                        <th>Categoria</th>
                        <th>Meta</th>
                        <th>Arrecadado</th>
                        <th>Status</th>
                        <th>Ações</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr v-for="campanha in campanhas" :key="campanha.id">
                        <td>{{ campanha.titulo }}</td>
                        <td>{{ campanha.categoria }}</td>
                        <td>R$ {{ formatarValor(campanha.metaFinanceira) }}</td>
                        <td>
                          R$ {{ formatarValor(campanha.valorArrecadado) }}
                        </td>
                        <td>
                          <span :class="getStatusClass(campanha.status)">{{
                            campanha.status
                          }}</span>
                        </td>
                        <td>
                          <div class="btn-group">
                            <button
                              class="btn btn-sm btn-outline-primary"
                              @click="editarCampanha(campanha)"
                            >
                              <i class="fas fa-edit"></i>
                            </button>
                            <button
                              class="btn btn-sm btn-outline-danger"
                              @click="excluirCampanha(campanha)"
                            >
                              <i class="fas fa-trash"></i>
                            </button>
                          </div>
                        </td>
                      </tr>
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
          </div>

          <!-- Aba de Doações -->
          <div
            class="tab-pane fade"
            id="doacoes"
            role="tabpanel"
            aria-labelledby="doacoes-tab"
          >
            <div class="card border-top-0 rounded-top-0">
              <div class="card-body">
                <h5 class="card-title mb-3">Gerenciar Doações</h5>
                <div class="table-responsive">
                  <table class="table table-hover">
                    <thead>
                      <tr>
                        <th>Campanha</th>
                        <th>Doador</th>
                        <th>Valor</th>
                        <th>Data</th>
                        <th>Status</th>
                        <th>Ações</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr v-for="doacao in doacoes" :key="doacao.id">
                        <td>{{ doacao.campanha.titulo }}</td>
                        <td>
                          {{ doacao.doador ? doacao.doador.nome : "Anônimo" }}
                        </td>
                        <td>R$ {{ formatarValor(doacao.valor) }}</td>
                        <td>{{ formatarData(doacao.dataHora) }}</td>
                        <td>
                          <span :class="getStatusClass(doacao.status)">{{
                            doacao.status
                          }}</span>
                        </td>
                        <td>
                          <div class="btn-group">
                            <button
                              class="btn btn-sm btn-outline-primary"
                              @click="verDoacao(doacao)"
                            >
                              <i class="fas fa-eye"></i>
                            </button>
                            <button
                              class="btn btn-sm btn-outline-success"
                              @click="confirmarDoacao(doacao)"
                              v-if="doacao.status === 'PENDENTE'"
                            >
                              <i class="fas fa-check"></i>
                            </button>
                            <button
                              class="btn btn-sm btn-outline-danger"
                              @click="cancelarDoacao(doacao)"
                              v-if="doacao.status !== 'CANCELADA'"
                            >
                              <i class="fas fa-times"></i>
                            </button>
                          </div>
                        </td>
                      </tr>
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
          </div>

          <!-- Aba de Transparência -->
          <div
            class="tab-pane fade"
            id="transparencia"
            role="tabpanel"
            aria-labelledby="transparencia-tab"
          >
            <div class="card border-top-0 rounded-top-0">
              <div class="card-body">
                <div
                  class="d-flex justify-content-between align-items-center mb-3"
                >
                  <h5 class="card-title">Gerenciar Transparência</h5>
                  <button class="btn btn-primary" @click="novaAlocacao">
                    <i class="fas fa-plus"></i> Nova Alocação
                  </button>
                </div>
                <div class="table-responsive">
                  <table class="table table-hover">
                    <thead>
                      <tr>
                        <th>Campanha</th>
                        <th>Título</th>
                        <th>Valor</th>
                        <th>Data</th>
                        <th>Responsável</th>
                        <th>Ações</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr v-for="(alocacao, index) in alocacoes" :key="index">
                        <td>{{ alocacao.campanha.titulo }}</td>
                        <td>{{ alocacao.tituloAlocacao }}</td>
                        <td>R$ {{ formatarValor(alocacao.valorAlocado) }}</td>
                        <td>{{ formatarData(alocacao.dataAlocacao) }}</td>
                        <td>{{ alocacao.responsavel.nome }}</td>
                        <td>
                          <div class="btn-group">
                            <button
                              class="btn btn-sm btn-outline-primary"
                              @click="verAlocacao(alocacao)"
                            >
                              <i class="fas fa-eye"></i>
                            </button>
                            <button
                              class="btn btn-sm btn-outline-secondary"
                              @click="editarAlocacao(alocacao)"
                            >
                              <i class="fas fa-edit"></i>
                            </button>
                            <button
                              class="btn btn-sm btn-outline-danger"
                              @click="excluirAlocacao(alocacao)"
                            >
                              <i class="fas fa-trash"></i>
                            </button>
                          </div>
                        </td>
                      </tr>
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
          </div>

          <!-- Aba de Usuários -->
          <div
            class="tab-pane fade"
            id="usuarios"
            role="tabpanel"
            aria-labelledby="usuarios-tab"
          >
            <div class="card border-top-0 rounded-top-0">
              <div class="card-body">
                <div
                  class="d-flex justify-content-between align-items-center mb-3"
                >
                  <h5 class="card-title">Gerenciar Usuários</h5>
                  <button class="btn btn-primary" @click="novoUsuario">
                    <i class="fas fa-plus"></i> Novo Administrador
                  </button>
                </div>
                <div class="table-responsive">
                  <table class="table table-hover">
                    <thead>
                      <tr>
                        <th>Nome</th>
                        <th>Email</th>
                        <th>Tipo</th>
                        <th>Data Cadastro</th>
                        <th>Status</th>
                        <th>Ações</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr v-for="usuario in usuarios" :key="usuario.id">
                        <td>{{ usuario.nome }}</td>
                        <td>{{ usuario.email }}</td>
                        <td>{{ usuario.tipo }}</td>
                        <td>{{ formatarData(usuario.dataCadastro) }}</td>
                        <td>
                          <span
                            :class="
                              usuario.ativo
                                ? 'badge bg-success'
                                : 'badge bg-danger'
                            "
                          >
                            {{ usuario.ativo ? "Ativo" : "Inativo" }}
                          </span>
                        </td>
                        <td>
                          <div class="btn-group">
                            <button
                              class="btn btn-sm btn-outline-primary"
                              @click="verUsuario(usuario)"
                            >
                              <i class="fas fa-eye"></i>
                            </button>
                            <button
                              class="btn btn-sm btn-outline-warning"
                              @click="alterarStatus(usuario)"
                            >
                              <i
                                :class="
                                  usuario.ativo ? 'fas fa-ban' : 'fas fa-check'
                                "
                              ></i>
                            </button>
                          </div>
                        </td>
                      </tr>
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "AdminView",
  data() {
    return {
      estatisticas: {
        campanhasAtivas: 5,
        totalArrecadado: 15750.0,
        totalDoacoes: 87,
        totalUsuarios: 42,
      },
      campanhas: [
        {
          id: 1,
          titulo: "Campanha de Saúde",
          categoria: "saude",
          metaFinanceira: 10000.0,
          valorArrecadado: 7500.0,
          status: "ATIVA",
        },
        {
          id: 2,
          titulo: "Educação para Todos",
          categoria: "educacao",
          metaFinanceira: 5000.0,
          valorArrecadado: 3250.0,
          status: "ATIVA",
        },
        {
          id: 3,
          titulo: "Ajuda Emergencial",
          categoria: "emergencia",
          metaFinanceira: 8000.0,
          valorArrecadado: 5000.0,
          status: "ATIVA",
        },
      ],
      doacoes: [
        {
          id: 1,
          campanha: { id: 1, titulo: "Campanha de Saúde" },
          doador: { id: 1, nome: "João Silva" },
          valor: 100.0,
          dataHora: "2025-05-15T10:30:00",
          status: "CONFIRMADA",
        },
        {
          id: 2,
          campanha: { id: 2, titulo: "Educação para Todos" },
          doador: { id: 2, nome: "Maria Oliveira" },
          valor: 50.0,
          dataHora: "2025-05-10T14:45:00",
          status: "CONFIRMADA",
        },
        {
          id: 3,
          campanha: { id: 3, titulo: "Ajuda Emergencial" },
          doador: null,
          valor: 200.0,
          dataHora: "2025-05-20T09:15:00",
          status: "PENDENTE",
        },
      ],
      alocacoes: [
        {
          id: 1,
          campanha: { id: 1, titulo: "Campanha de Saúde" },
          tituloAlocacao: "Compra de Medicamentos",
          valorAlocado: 2500.0,
          dataAlocacao: "2025-05-18T11:30:00",
          responsavel: { id: 3, nome: "Admin Sistema" },
        },
        {
          id: 2,
          campanha: { id: 2, titulo: "Educação para Todos" },
          tituloAlocacao: "Material Escolar",
          valorAlocado: 1500.0,
          dataAlocacao: "2025-05-12T16:45:00",
          responsavel: { id: 3, nome: "Admin Sistema" },
        },
      ],
      usuarios: [
        {
          id: 1,
          nome: "João Silva",
          email: "joao@example.com",
          tipo: "DOADOR",
          dataCadastro: "2025-04-10T08:30:00",
          ativo: true,
        },
        {
          id: 2,
          nome: "Maria Oliveira",
          email: "maria@example.com",
          tipo: "DOADOR",
          dataCadastro: "2025-04-15T14:20:00",
          ativo: true,
        },
        {
          id: 3,
          nome: "Admin Sistema",
          email: "admin@example.com",
          tipo: "ADMINISTRADOR",
          dataCadastro: "2025-04-01T10:00:00",
          ativo: true,
        },
      ],
    };
  },
  methods: {
    formatarValor(valor) {
      return valor
        ? valor.toLocaleString("pt-BR", {
            minimumFractionDigits: 2,
            maximumFractionDigits: 2,
          })
        : "0,00";
    },
    formatarData(data) {
      if (!data) return "-";
      const dataObj = new Date(data);
      return dataObj.toLocaleDateString("pt-BR");
    },
    getStatusClass(status) {
      const classes = {
        ATIVA: "badge bg-success",
        FINALIZADA: "badge bg-secondary",
        CANCELADA: "badge bg-danger",
        CONFIRMADA: "badge bg-success",
        PENDENTE: "badge bg-warning text-dark",
      };
      return classes[status] || "badge bg-secondary";
    },
    novaCampanha() {
      alert("Funcionalidade de criar nova campanha será implementada!");
    },
    editarCampanha(campanha) {
      alert(`Editar campanha: ${campanha.titulo}`);
    },
    excluirCampanha(campanha) {
      if (
        confirm(
          `Tem certeza que deseja excluir a campanha "${campanha.titulo}"?`
        )
      ) {
        alert("Campanha excluída com sucesso!");
      }
    },
    verDoacao(doacao) {
      alert(`Ver detalhes da doação: ${doacao.id}`);
    },
    confirmarDoacao(doacao) {
      if (
        confirm(`Confirmar doação de R$ ${this.formatarValor(doacao.valor)}?`)
      ) {
        alert("Doação confirmada com sucesso!");
        doacao.status = "CONFIRMADA";
      }
    },
    cancelarDoacao(doacao) {
      if (
        confirm(`Cancelar doação de R$ ${this.formatarValor(doacao.valor)}?`)
      ) {
        alert("Doação cancelada com sucesso!");
        doacao.status = "CANCELADA";
      }
    },
    novaAlocacao() {
      alert("Funcionalidade de criar nova alocação será implementada!");
    },
    verAlocacao(alocacao) {
      alert(`Ver detalhes da alocação: ${alocacao.tituloAlocacao}`);
    },
    editarAlocacao(alocacao) {
      alert(`Editar alocação: ${alocacao.tituloAlocacao}`);
    },
    excluirAlocacao(alocacao) {
      if (
        confirm(
          `Tem certeza que deseja excluir a alocação "${alocacao.tituloAlocacao}"?`
        )
      ) {
        alert("Alocação excluída com sucesso!");
      }
    },
    novoUsuario() {
      alert("Funcionalidade de criar novo administrador será implementada!");
    },
    verUsuario(usuario) {
      alert(`Ver detalhes do usuário: ${usuario.nome}`);
    },
    alterarStatus(usuario) {
      const novoStatus = usuario.ativo ? "inativar" : "ativar";
      if (
        confirm(
          `Tem certeza que deseja ${novoStatus} o usuário "${usuario.nome}"?`
        )
      ) {
        usuario.ativo = !usuario.ativo;
        alert(
          `Usuário ${
            novoStatus === "ativar" ? "ativado" : "inativado"
          } com sucesso!`
        );
      }
    },
  },
};
</script>

<style scoped>
.admin-view {
  padding-top: 20px;
  padding-bottom: 30px;
}

.nav-tabs .nav-link {
  color: #495057;
}

.nav-tabs .nav-link.active {
  color: #007bff;
  font-weight: 500;
}
</style>
