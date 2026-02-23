<template>
  <div class="admin-view">
    <div class="row mb-4">
      <div class="col-12">
        <h1 class="mb-3">Painel Administrativo</h1>
      </div>
    </div>

    <!-- Loading state -->
    <div v-if="loading" class="text-center py-5">
      <div class="spinner-border text-primary" role="status">
        <span class="visually-hidden">Carregando...</span>
      </div>
      <p class="mt-3">Carregando dados administrativos...</p>
    </div>

    <div v-else>
      <!-- Cards de estatísticas -->
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

      <!-- Abas de navegação -->
      <div class="row">
        <div class="col-12">
          <ul class="nav nav-tabs" id="adminTab" role="tablist">
            <li class="nav-item" role="presentation">
              <button
                :class="['nav-link', { active: currentTab === 'campanhas' }]"
                id="campanhas-tab"
                data-bs-toggle="tab"
                data-bs-target="#campanhas"
                type="button"
                role="tab"
                aria-controls="campanhas"
                :aria-selected="currentTab === 'campanhas'"
                @click.prevent="setTab('campanhas')"
              >
                Campanhas
              </button>
            </li>
            <li class="nav-item" role="presentation">
              <button
                :class="['nav-link', { active: currentTab === 'doacoes' }]"
                id="doacoes-tab"
                data-bs-toggle="tab"
                data-bs-target="#doacoes"
                type="button"
                role="tab"
                aria-controls="doacoes"
                :aria-selected="currentTab === 'doacoes'"
                @click.prevent="setTab('doacoes')"
              >
                Doações
              </button>
            </li>
            <li class="nav-item" role="presentation">
              <button
                :class="[
                  'nav-link',
                  { active: currentTab === 'transparencia' },
                ]"
                id="transparencia-tab"
                data-bs-toggle="tab"
                data-bs-target="#transparencia"
                type="button"
                role="tab"
                aria-controls="transparencia"
                :aria-selected="currentTab === 'transparencia'"
                @click.prevent="setTab('transparencia')"
              >
                Transparência
              </button>
            </li>
            <li class="nav-item" role="presentation">
              <button
                :class="['nav-link', { active: currentTab === 'usuarios' }]"
                id="usuarios-tab"
                data-bs-toggle="tab"
                data-bs-target="#usuarios"
                type="button"
                role="tab"
                aria-controls="usuarios"
                :aria-selected="currentTab === 'usuarios'"
                @click.prevent="setTab('usuarios')"
              >
                Usuários
              </button>
            </li>
            <li class="nav-item" role="presentation">
              <button
                :class="['nav-link', { active: currentTab === 'mensagens' }]"
                id="mensagens-tab"
                data-bs-toggle="tab"
                data-bs-target="#mensagens"
                type="button"
                role="tab"
                aria-controls="mensagens"
                :aria-selected="currentTab === 'mensagens'"
                @click.prevent="setTab('mensagens')"
              >
                Mensagens
              </button>
            </li>
          </ul>
          <div class="tab-content" id="adminTabContent">
            <!-- Aba de Campanhas -->
            <div
              :class="[
                'tab-pane',
                'fade',
                {
                  show: currentTab === 'campanhas',
                  active: currentTab === 'campanhas',
                },
              ]"
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
                    <div>
                      <!-- TODO: Validar se faz sentido ter uma tela de campanhas (ela existe não mas não esta completa) 
                      <router-link
                        to="/admin/campanhas"
                        class="btn btn-info me-2"
                      >
                        <i class="fas fa-cog"></i> Painel de Campanhas
                      </router-link> -->
                      <button class="btn btn-primary" @click="novaCampanha">
                        <i class="fas fa-plus"></i> Nova Campanha
                      </button>
                    </div>
                  </div>

                  <!-- Loading state para campanhas -->
                  <div v-if="loadingCampanhas" class="text-center py-4">
                    <div
                      class="spinner-border spinner-border-sm text-primary"
                      role="status"
                    ></div>
                    <span class="ms-2">Carregando campanhas...</span>
                  </div>

                  <!-- Tabela de campanhas -->
                  <div v-else class="table-responsive">
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
                        <tr v-if="campanhas.length === 0">
                          <td colspan="6" class="text-center py-3">
                            Nenhuma campanha encontrada
                          </td>
                        </tr>
                        <tr v-for="campanha in campanhas" :key="campanha.id">
                          <td>{{ campanha.titulo }}</td>
                          <td>{{ campanha.categoria }}</td>
                          <td>
                            R$ {{ formatarValor(campanha.metaFinanceira) }}
                          </td>
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
              :class="[
                'tab-pane',
                'fade',
                {
                  show: currentTab === 'doacoes',
                  active: currentTab === 'doacoes',
                },
              ]"
              id="doacoes"
              role="tabpanel"
              aria-labelledby="doacoes-tab"
            >
              <div class="card border-top-0 rounded-top-0">
                <div class="card-body">
                  <h5 class="card-title mb-3">Gerenciar Doações</h5>

                  <!-- Loading state para doações -->
                  <div v-if="loadingDoacoes" class="text-center py-4">
                    <div
                      class="spinner-border spinner-border-sm text-primary"
                      role="status"
                    ></div>
                    <span class="ms-2">Carregando doações...</span>
                  </div>

                  <!-- Tabela de doações -->
                  <div v-else class="table-responsive">
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
                        <tr v-if="doacoes.length === 0">
                          <td colspan="6" class="text-center py-3">
                            Nenhuma doação encontrada
                          </td>
                        </tr>
                        <tr v-for="doacao in doacoes" :key="doacao.id">
                          <td>{{ doacao.campanha.titulo }}</td>
                          <td>
                            {{
                              doacao.anonimo
                                ? "Anônimo"
                                : doacao.doador
                                ? doacao.doador.nome
                                : "N/A"
                            }}
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
              :class="[
                'tab-pane',
                'fade',
                {
                  show: currentTab === 'transparencia',
                  active: currentTab === 'transparencia',
                },
              ]"
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

                  <!-- Loading state para alocações -->
                  <div v-if="loadingAlocacoes" class="text-center py-4">
                    <div
                      class="spinner-border spinner-border-sm text-primary"
                      role="status"
                    ></div>
                    <span class="ms-2">Carregando alocações...</span>
                  </div>

                  <!-- Tabela de alocações -->
                  <div v-else class="table-responsive">
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
                        <tr v-if="alocacoes.length === 0">
                          <td colspan="6" class="text-center py-3">
                            Nenhuma alocação encontrada
                          </td>
                        </tr>
                        <tr v-for="alocacao in alocacoes" :key="alocacao.id">
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
              :class="[
                'tab-pane',
                'fade',
                {
                  show: currentTab === 'usuarios',
                  active: currentTab === 'usuarios',
                },
              ]"
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

                  <!-- Loading state para usuários -->
                  <div v-if="loadingUsuarios" class="text-center py-4">
                    <div
                      class="spinner-border spinner-border-sm text-primary"
                      role="status"
                    ></div>
                    <span class="ms-2">Carregando usuários...</span>
                  </div>

                  <!-- Tabela de usuários -->
                  <div v-else class="table-responsive">
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
                        <tr v-if="usuarios.length === 0">
                          <td colspan="6" class="text-center py-3">
                            Nenhum usuário encontrado
                          </td>
                        </tr>
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
                                <!-- TODO: Essa ação não existe no backend -->
                                <i
                                  :class="
                                    usuario.ativo
                                      ? 'fas fa-ban'
                                      : 'fas fa-check'
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
          <!-- Aba de Mensagens -->
          <div
            :class="[
              'tab-pane',
              'fade',
              {
                show: currentTab === 'mensagens',
                active: currentTab === 'mensagens',
              },
            ]"
            id="mensagens"
            role="tabpanel"
            aria-labelledby="mensagens-tab"
          >
            <div class="card border-top-0 rounded-top-0">
              <div class="card-body">
                <div
                  class="d-flex justify-content-between align-items-center mb-3"
                >
                  <h5 class="card-title">Mensagens de Contato</h5>
                </div>

                <div v-if="loadingMensagens" class="text-center py-4">
                  <div
                    class="spinner-border spinner-border-sm text-primary"
                    role="status"
                  ></div>
                  <span class="ms-2">Carregando mensagens...</span>
                </div>

                <div v-else class="table-responsive">
                  <table class="table table-hover">
                    <thead>
                      <tr>
                        <th>Email</th>
                        <th>Título</th>
                        <th>Descrição</th>
                        <th>Data</th>
                        <th>Ações</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr v-if="mensagens.length === 0">
                        <td colspan="5" class="text-center py-3">
                          Nenhuma mensagem encontrada
                        </td>
                      </tr>
                      <tr v-for="m in mensagens" :key="m.id">
                        <td>{{ m.email }}</td>
                        <td>{{ m.titulo }}</td>
                        <td>{{ m.descricao }}</td>
                        <td>{{ formatarData(m.dataEnvio) }}</td>
                        <td>
                          <div class="btn-group">
                            <button
                              class="btn btn-sm btn-outline-primary"
                              @click="verMensagem(m)"
                            >
                              <i class="fas fa-eye"></i>
                            </button>
                            <button
                              class="btn btn-sm btn-outline-danger"
                              @click="excluirMensagem(m)"
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
        </div>
      </div>
    </div>

    <!-- Modal de Nova Campanha -->
    <div
      class="modal fade"
      :class="{ 'show d-block': showModalCampanha }"
      tabindex="-1"
      :style="{ background: showModalCampanha ? 'rgba(0,0,0,0.5)' : '' }"
    >
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">
              {{ campanhaSelecionada.id ? "Editar" : "Nova" }} Campanha
            </h5>
            <button
              type="button"
              class="btn-close"
              @click="fecharModalCampanha"
            ></button>
          </div>
          <form @submit.prevent="salvarCampanha">
            <div class="modal-body">
              <div class="row mb-3">
                <div class="col-md-8">
                  <label for="titulo" class="form-label">Título</label>
                  <input
                    type="text"
                    class="form-control"
                    id="titulo"
                    v-model="campanhaSelecionada.titulo"
                    required
                  />
                </div>
                <div class="col-md-4">
                  <label for="categoria" class="form-label">Categoria</label>
                  <select
                    class="form-select"
                    id="categoria"
                    v-model="campanhaSelecionada.categoria"
                    required
                  >
                    <option value="">Selecione uma categoria</option>
                    <option value="saude">Saúde</option>
                    <option value="educacao">Educação</option>
                    <option value="emergencia">Emergência</option>
                    <option value="social">Social</option>
                    <option value="ambiental">Ambiental</option>
                  </select>
                </div>
              </div>

              <div class="row mb-3">
                <div class="col-md-6">
                  <label for="metaFinanceira" class="form-label"
                    >Meta Financeira (R$)</label
                  >
                  <input
                    type="number"
                    class="form-control"
                    id="metaFinanceira"
                    v-model="campanhaSelecionada.metaFinanceira"
                    min="0"
                    step="0.01"
                    required
                  />
                </div>
                <div class="col-md-6">
                  <label for="dataTermino" class="form-label"
                    >Data de Término
                  </label>
                  <input
                    type="date"
                    class="form-control"
                    id="dataTermino"
                    v-model="campanhaSelecionada.dataTermino"
                    required
                  />
                </div>
              </div>

              <div class="mb-3">
                <label for="imagemCapaUrl" class="form-label"
                  >URL da Imagem de Capa</label
                >
                <input
                  type="url"
                  class="form-control"
                  id="imagemCapaUrl"
                  v-model="campanhaSelecionada.imagemCapa"
                  placeholder="Cole a URL da imagem (opcional, será substituída se enviar arquivo)"
                />
                <div class="form-text mt-1">
                  Ou envie um arquivo de imagem abaixo (arquivo terá
                  prioridade).
                </div>
              </div>

              <div class="mb-3">
                <label for="imagemCapaFile" class="form-label"
                  >Enviar Imagem</label
                >
                <input
                  id="imagemCapaFile"
                  class="form-control"
                  type="file"
                  accept="image/*"
                  @change="onCampanhaFileChange"
                />
                <div v-if="campanhaImagemFileName" class="form-text mt-1">
                  Arquivo selecionado: {{ campanhaImagemFileName }}
                </div>
                <!-- Image preview: shows selected file or the URL from imagemCapa -->
                <div v-if="campanhaImagemPreview" class="mt-3">
                  <label class="form-label d-block">Preview da Imagem</label>
                  <div style="max-width: 320px">
                    <img
                      :src="campanhaImagemPreview"
                      alt="Preview imagem da campanha"
                      class="img-fluid rounded"
                      style="
                        max-height: 200px;
                        object-fit: contain;
                        width: 100%;
                        border: 1px solid #e9ecef;
                        padding: 4px;
                        background: #fff;
                      "
                    />
                  </div>
                  <div class="form-text mt-1">
                    A imagem acima será enviada (se você escolher um arquivo) ou
                    usada pela URL informada.
                  </div>
                </div>
              </div>

              <div class="mb-3">
                <label for="descricao" class="form-label">Descrição</label>
                <textarea
                  class="form-control"
                  id="descricao"
                  v-model="campanhaSelecionada.descricao"
                  rows="5"
                  required
                ></textarea>
              </div>

              <div class="mb-3">
                <label for="beneficiarios" class="form-label"
                  >Beneficiários</label
                >
                <textarea
                  class="form-control"
                  id="beneficiarios"
                  v-model="campanhaSelecionada.beneficiarios"
                  rows="3"
                  required
                ></textarea>
              </div>

              <div class="mb-3">
                <label for="status" class="form-label">Status</label>
                <select
                  class="form-select"
                  id="status"
                  v-model="campanhaSelecionada.status"
                  required
                >
                  <option value="ATIVA">Ativa</option>
                  <option value="FINALIZADA">Finalizada</option>
                  <option value="CANCELADA">Cancelada</option>
                </select>
              </div>
              <!-- form intentionally kept open so modal-footer buttons are inside the form -->
            </div>
            <div class="modal-footer">
              <button
                type="button"
                class="btn btn-secondary"
                @click="fecharModalCampanha"
              >
                Cancelar
              </button>
              <button
                type="submit"
                class="btn btn-primary"
                :disabled="salvandoCampanha"
              >
                <span
                  v-if="salvandoCampanha"
                  class="spinner-border spinner-border-sm me-2"
                  role="status"
                ></span>
                Salvar
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>

    <!-- Modal de Nova Alocação -->
    <div
      class="modal fade"
      :class="{ 'show d-block': showModalAlocacao }"
      tabindex="-1"
      :style="{ background: showModalAlocacao ? 'rgba(0,0,0,0.5)' : '' }"
    >
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">
              {{ alocacaoSelecionada.id ? "Editar" : "Nova" }} Alocação
            </h5>
            <button
              type="button"
              class="btn-close"
              @click="fecharModalAlocacao"
            ></button>
          </div>
          <div class="modal-body">
            <form @submit.prevent="salvarAlocacao">
              <div class="mb-3">
                <label for="campanha" class="form-label">Campanha</label>
                <select
                  class="form-select"
                  id="campanha"
                  v-model="alocacaoSelecionada.campanha"
                  required
                >
                  <option value="">Selecione uma campanha</option>
                  <option
                    v-for="campanha in campanhas"
                    :key="campanha.id"
                    :value="campanha"
                  >
                    {{ campanha.titulo }}
                  </option>
                </select>
              </div>

              <div class="mb-3">
                <label for="tituloAlocacao" class="form-label"
                  >Título da Alocação</label
                >
                <input
                  type="text"
                  class="form-control"
                  id="tituloAlocacao"
                  v-model="alocacaoSelecionada.tituloAlocacao"
                  required
                />
              </div>

              <div class="mb-3">
                <label for="descricaoAlocacao" class="form-label"
                  >Descrição</label
                >
                <textarea
                  class="form-control"
                  id="descricaoAlocacao"
                  v-model="alocacaoSelecionada.descricaoAlocacao"
                  rows="3"
                ></textarea>
              </div>

              <div class="mb-3">
                <label for="valorAlocado" class="form-label"
                  >Valor Alocado (R$)</label
                >
                <input
                  type="number"
                  class="form-control"
                  id="valorAlocado"
                  v-model="alocacaoSelecionada.valorAlocado"
                  min="0"
                  step="0.01"
                  required
                />
              </div>

              <div class="mb-3">
                <label for="comprovante" class="form-label"
                  >URL do Comprovante</label
                >
                <input
                  type="url"
                  class="form-control"
                  id="comprovante"
                  v-model="alocacaoSelecionada.comprovante"
                />
              </div>
            </form>
          </div>
          <div class="modal-footer">
            <button
              type="button"
              class="btn btn-secondary"
              @click="fecharModalAlocacao"
            >
              Cancelar
            </button>
            <button
              type="button"
              class="btn btn-primary"
              @click="salvarAlocacao"
              :disabled="salvandoAlocacao"
            >
              <span
                v-if="salvandoAlocacao"
                class="spinner-border spinner-border-sm me-2"
                role="status"
              ></span>
              Salvar
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Modal de Novo Usuário -->
    <div
      class="modal fade"
      :class="{ 'show d-block': showModalUsuario }"
      tabindex="-1"
      :style="{ background: showModalUsuario ? 'rgba(0,0,0,0.5)' : '' }"
    >
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">Novo Administrador</h5>
            <button
              type="button"
              class="btn-close"
              @click="fecharModalUsuario"
            ></button>
          </div>
          <div class="modal-body">
            <form @submit.prevent="salvarUsuario">
              <div class="mb-3">
                <label for="nomeUsuario" class="form-label"
                  >Nome Completo</label
                >
                <input
                  type="text"
                  class="form-control"
                  id="nomeUsuario"
                  v-model="usuarioSelecionado.nome"
                  required
                />
              </div>

              <div class="mb-3">
                <label for="emailUsuario" class="form-label">Email</label>
                <input
                  type="email"
                  class="form-control"
                  id="emailUsuario"
                  v-model="usuarioSelecionado.email"
                  required
                />
              </div>

              <div class="mb-3">
                <label for="senhaUsuario" class="form-label">Senha</label>
                <input
                  type="password"
                  class="form-control"
                  id="senhaUsuario"
                  v-model="usuarioSelecionado.senha"
                  required
                  minlength="6"
                />
                <div class="form-text">
                  A senha deve ter pelo menos 6 caracteres.
                </div>
              </div>

              <div class="mb-3">
                <label for="confirmarSenha" class="form-label"
                  >Confirmar Senha</label
                >
                <input
                  type="password"
                  class="form-control"
                  id="confirmarSenha"
                  v-model="usuarioSelecionado.confirmarSenha"
                  required
                />
              </div>
            </form>
          </div>
          <div class="modal-footer">
            <button
              type="button"
              class="btn btn-secondary"
              @click="fecharModalUsuario"
            >
              Cancelar
            </button>
            <button
              type="button"
              class="btn btn-primary"
              @click="salvarUsuario"
              :disabled="salvandoUsuario || !senhasIguais"
            >
              <span
                v-if="salvandoUsuario"
                class="spinner-border spinner-border-sm me-2"
                role="status"
              ></span>
              Salvar
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch, onBeforeUnmount } from "vue";
import { useRoute, useRouter } from "vue-router";
import axios from "axios";
import { formatarData, formatarValor } from "../utils";

const loading = ref(true);
const loadingCampanhas = ref(false);
const loadingDoacoes = ref(false);
const loadingAlocacoes = ref(false);
const loadingUsuarios = ref(false);
const loadingMensagens = ref(false);

const estatisticas = reactive({
  campanhasAtivas: 0,
  totalArrecadado: 0,
  totalDoacoes: 0,
  totalUsuarios: 0,
});

const campanhas = ref([]);
const doacoes = ref([]);
const alocacoes = ref([]);
const usuarios = ref([]);
const mensagens = ref([]);

const showModalCampanha = ref(false);
const showModalAlocacao = ref(false);
const showModalUsuario = ref(false);

const campanhaSelecionada = reactive({
  id: null,
  titulo: "",
  categoria: "",
  metaFinanceira: 0,
  dataTermino: "",
  imagemCapa: "",
  descricao: "",
  beneficiarios: "",
  status: "ATIVA",
});

const alocacaoSelecionada = reactive({
  id: null,
  campanha: null,
  tituloAlocacao: "",
  descricaoAlocacao: "",
  valorAlocado: 0,
  comprovante: "",
});

const usuarioSelecionado = reactive({
  id: null,
  nome: "",
  email: "",
  senha: "",
  confirmarSenha: "",
});

const salvandoCampanha = ref(false);
const salvandoAlocacao = ref(false);
const salvandoUsuario = ref(false);

const senhasIguais = () =>
  usuarioSelecionado.senha === usuarioSelecionado.confirmarSenha;

const campanhaImagemFile = ref(null);
const campanhaImagemFileName = ref("");
const campanhaImagemPreview = ref("");

let _currentObjectUrl = null;

const carregarDados = async () => {
  loading.value = true;
  try {
    await Promise.all([
      carregarEstatisticas(),
      carregarCampanhas(),
      carregarDoacoes(),
      carregarAlocacoes(),
      carregarUsuarios(),
      carregarMensagens(),
    ]);
  } catch (e) {
    console.error("Erro ao carregar dados:", e);
  } finally {
    loading.value = false;
  }
};

const carregarMensagens = async () => {
  loadingMensagens.value = true;
  try {
    const resp = await axios.get(
      `${process.env.VUE_APP_API_BASE_URL}/api/contato`,
    );
    mensagens.value = resp.data;
  } catch (e) {
    console.error("Erro ao carregar mensagens:", e);
    mensagens.value = [];
  } finally {
    loadingMensagens.value = false;
  }
};

function verMensagem(m) {
  alert(`De: ${m.email}\nTítulo: ${m.titulo}\n\n${m.descricao}`);
}

async function excluirMensagem(m) {
  if (confirm(`Excluir mensagem de ${m.email}?`)) {
    try {
      await axios.delete(
        `${process.env.VUE_APP_API_BASE_URL}/api/contato/${m.id}`,
      );
      await carregarMensagens();
      alert("Mensagem excluída com sucesso");
    } catch (e) {
      console.error("Erro ao excluir mensagem", e);
      alert("Erro ao excluir mensagem");
    }
  }
}

const carregarEstatisticas = async () => {
  try {
    const resp = await axios.get(
      `${process.env.VUE_APP_API_BASE_URL}/api/admin/estatisticas`,
    );
    Object.assign(estatisticas, resp.data);
  } catch (e) {
    console.error("Erro ao carregar estatisticas:", e);
  }
};

const carregarCampanhas = async () => {
  loadingCampanhas.value = true;
  try {
    const resp = await axios.get(
      `${process.env.VUE_APP_API_BASE_URL}/api/campanhas`,
    );
    campanhas.value = resp.data;
  } catch (e) {
    console.error("Erro ao carregar campanhas:", e);
    campanhas.value = [];
  } finally {
    loadingCampanhas.value = false;
  }
};

const carregarDoacoes = async () => {
  loadingDoacoes.value = true;
  try {
    const resp = await axios.get(
      `${process.env.VUE_APP_API_BASE_URL}/api/admin/doacoes`,
    );
    doacoes.value = resp.data;
  } catch (e) {
    console.error("Erro ao carregar doacoes:", e);
    doacoes.value = [];
  } finally {
    loadingDoacoes.value = false;
  }
};

const carregarAlocacoes = async () => {
  loadingAlocacoes.value = true;
  try {
    const resp = await axios.get(
      `${process.env.VUE_APP_API_BASE_URL}/api/transparencia/publica`,
    );
    alocacoes.value = resp.data;
  } catch (e) {
    console.error("Erro ao carregar alocacoes:", e);
    alocacoes.value = [];
  } finally {
    loadingAlocacoes.value = false;
  }
};

const carregarUsuarios = async () => {
  loadingUsuarios.value = true;
  try {
    const resp = await axios.get(
      `${process.env.VUE_APP_API_BASE_URL}/api/usuarios`,
    );
    usuarios.value = resp.data;
  } catch (e) {
    console.error("Erro ao carregar usuarios:", e);
    usuarios.value = [];
  } finally {
    loadingUsuarios.value = false;
  }
};

const route = useRoute();
const router = useRouter();

const validTabs = [
  "campanhas",
  "doacoes",
  "transparencia",
  "usuarios",
  "mensagens",
];

const currentTab = ref("campanhas");

function setTab(tab) {
  if (!validTabs.includes(tab)) tab = "campanhas";
  if (currentTab.value === tab) return;
  currentTab.value = tab;

  router
    .replace({
      name: route.name,
      params: route.params,
      query: { ...route.query, tab: tab },
    })
    .catch(() => {});
}

watch(
  () => route.query && route.query.tab,
  (newTab) => {
    if (newTab && typeof newTab === "string" && validTabs.includes(newTab)) {
      currentTab.value = newTab;
    }
  },
);

watch(currentTab, (tab) => {
  if (!tab) return;
  if ((route.query && route.query.tab) === tab) return;
  router
    .replace({
      name: route.name,
      params: route.params,
      query: { ...route.query, tab },
    })
    .catch(() => {});
});

onMounted(() => {
  const qTab = (route.query && route.query.tab) || null;
  if (qTab && typeof qTab === "string" && validTabs.includes(qTab)) {
    currentTab.value = qTab;
  } else {
    router
      .replace({
        name: route.name,
        params: route.params,
        query: { ...route.query, tab: currentTab.value },
      })
      .catch(() => {});
  }
  carregarDados();
});

function novaCampanha() {
  campanhaSelecionada.value = {
    titulo: "",
    categoria: "",
    metaFinanceira: 0,
    dataTermino: formatarDataInput(
      new Date(Date.now() + 30 * 24 * 60 * 60 * 1000),
    ),
    imagemCapa: "",
    descricao: "",
    beneficiarios: "",
    status: "ATIVA",
  };
  showModalCampanha.value = true;

  campanhaImagemPreview.value = "";
}

function onCampanhaFileChange(e) {
  const file = e.target.files && e.target.files[0];
  if (file) {
    campanhaImagemFile.value = file;
    campanhaImagemFileName.value = file.name;

    if (_currentObjectUrl) {
      try {
        URL.revokeObjectURL(_currentObjectUrl);
      } catch (err) {
        //
      }
      _currentObjectUrl = null;
    }
    _currentObjectUrl = URL.createObjectURL(file);
    campanhaImagemPreview.value = _currentObjectUrl;
  } else {
    campanhaImagemFile.value = null;
    campanhaImagemFileName.value = "";

    if (_currentObjectUrl) {
      try {
        URL.revokeObjectURL(_currentObjectUrl);
      } catch (err) {
        //
      }
      _currentObjectUrl = null;
    }

    campanhaImagemPreview.value = campanhaSelecionada.imagemCapa || "";
  }
}

const editarCampanha = (c) => {
  Object.assign(campanhaSelecionada, c);
  showModalCampanha.value = true;
  campanhaImagemFile.value = null;
  campanhaImagemFileName.value = "";

  if (_currentObjectUrl) {
    try {
      URL.revokeObjectURL(_currentObjectUrl);
    } catch (err) {
      //
    }
    _currentObjectUrl = null;
  }
  campanhaImagemPreview.value = c.imagemCapa || "";
};

const getStatusClass = (status) =>
  ({
    ATIVA: "badge bg-success",
    FINALIZADA: "badge bg-secondary",
    CANCELADA: "badge bg-danger",
    CONFIRMADA: "badge bg-success",
    PENDENTE: "badge bg-warning text-dark",
  }[status] || "badge bg-secondary");

async function salvarCampanha() {
  salvandoCampanha.value = true;

  try {
    const campanhaPayload = JSON.stringify(campanhaSelecionada);
    const form = new FormData();
    form.append(
      "campanha",
      new Blob([campanhaPayload], { type: "application/json" }),
    );
    if (campanhaImagemFile.value) {
      form.append("imagem", campanhaImagemFile.value);
    }

    if (campanhaSelecionada.id) {
      await axios.put(
        `${process.env.VUE_APP_API_BASE_URL}/api/campanhas/${campanhaSelecionada.id}`,
        form,
        { headers: { "Content-Type": "multipart/form-data" } },
      );
    } else {
      await axios.post(
        `${process.env.VUE_APP_API_BASE_URL}/api/campanhas`,
        form,
      );
    }

    await carregarCampanhas();
    fecharModalCampanha();

    alert(
      campanhaSelecionada.id
        ? "Campanha atualizada com sucesso!"
        : "Campanha criada com sucesso!",
    );
  } catch (error) {
    console.error("Erro ao salvar campanha:", error);
    alert("Erro ao salvar campanha. Por favor, tente novamente.");
  } finally {
    salvandoCampanha.value = false;
  }
}

async function excluirCampanha(campanha) {
  if (
    confirm(`Tem certeza que deseja excluir a campanha "${campanha.titulo}"?`)
  ) {
    try {
      await axios.delete(
        `${process.env.VUE_APP_API_BASE_URL}/api/campanhas/${campanha.id}`,
      );

      await carregarCampanhas();

      alert("Campanha excluída com sucesso!");
    } catch (error) {
      console.error("Erro ao excluir campanha:", error);
      alert("Erro ao excluir campanha. Por favor, tente novamente.");
    }
  }
}

function fecharModalCampanha() {
  showModalCampanha.value = false;
  campanhaSelecionada.value = {
    titulo: "",
    categoria: "",
    metaFinanceira: 0,
    dataTermino: "",
    imagemCapa: "",
    descricao: "",
    beneficiarios: "",
    status: "ATIVA",
  };
  campanhaImagemFile.value = null;
  campanhaImagemFileName.value = "";

  if (_currentObjectUrl) {
    try {
      URL.revokeObjectURL(_currentObjectUrl);
    } catch (err) {
      //
    }
    _currentObjectUrl = null;
  }
  campanhaImagemPreview.value = "";
}

watch(
  () => campanhaSelecionada.imagemCapa,
  (newUrl) => {
    if (!campanhaImagemFile.value) {
      campanhaImagemPreview.value = newUrl || "";
    }
  },
);

onBeforeUnmount(() => {
  if (_currentObjectUrl) {
    try {
      URL.revokeObjectURL(_currentObjectUrl);
    } catch (err) {
      //
    }
    _currentObjectUrl = null;
  }
});

async function confirmarDoacao(doacao) {
  if (
    confirm(
      `Confirmar doação de R$ ${formatarValor(doacao.valor)} para a campanha "${
        doacao.campanha.titulo
      }"?`,
    )
  ) {
    try {
      await axios.put(
        `${process.env.VUE_APP_API_BASE_URL}/api/doacoes/${doacao.id}/confirmar`,
      );

      await carregarDoacoes();

      await carregarEstatisticas();

      alert("Doação confirmada com sucesso!");
    } catch (error) {
      console.error("Erro ao confirmar doação:", error);
      alert("Erro ao confirmar doação. Por favor, tente novamente.");
    }
  }
}

async function cancelarDoacao(doacao) {
  if (
    confirm(
      `Cancelar doação de R$ ${formatarValor(doacao.valor)} para a campanha "${
        doacao.campanha.titulo
      }"?`,
    )
  ) {
    try {
      await axios.put(
        `${process.env.VUE_APP_API_BASE_URL}/api/doacoes/${doacao.id}/cancelar`,
      );

      await carregarDoacoes();

      await carregarEstatisticas();

      alert("Doação cancelada com sucesso!");
    } catch (error) {
      console.error("Erro ao cancelar doação:", error);
      alert("Erro ao cancelar doação. Por favor, tente novamente.");
    }
  }
}

function verDoacao(doacao) {
  alert(
    `Detalhes da Doação:\n\nCampanha: ${doacao.campanha.titulo}\nDoador: ${
      doacao.anonimo ? "Anônimo" : doacao.doador ? doacao.doador.nome : "N/A"
    }\nValor: R$ ${formatarValor(doacao.valor)}\nData: ${formatarData(
      doacao.dataHora,
    )}\nStatus: ${doacao.status}\nMensagem: ${
      doacao.mensagem || "Nenhuma mensagem"
    }`,
  );
}

function novaAlocacao() {
  Object.assign(alocacaoSelecionada, {
    campanha: null,
    tituloAlocacao: "",
    descricaoAlocacao: "",
    valorAlocado: 0,
    comprovante: "",
  });
  showModalAlocacao.value = true;
}

function editarAlocacao(alocacao) {
  Object.assign(alocacaoSelecionada, { ...alocacao });
  showModalAlocacao.value = true;
}

async function salvarAlocacao() {
  salvandoAlocacao.value = true;

  try {
    if (alocacaoSelecionada.id) {
      await axios.put(
        `${process.env.VUE_APP_API_BASE_URL}/api/transparencia/${alocacaoSelecionada.id}`,
        alocacaoSelecionada,
      );
    } else {
      await axios.post(
        `${process.env.VUE_APP_API_BASE_URL}/api/transparencia`,
        alocacaoSelecionada,
      );
    }

    await carregarAlocacoes();
    fecharModalAlocacao();

    alert(
      alocacaoSelecionada.id
        ? "Alocação atualizada com sucesso!"
        : "Alocação criada com sucesso!",
    );
  } catch (error) {
    console.error("Erro ao salvar alocação:", error);
    alert("Erro ao salvar alocação. Por favor, tente novamente.");
  } finally {
    salvandoAlocacao.value = false;
  }
}

function verAlocacao(alocacao) {
  alert(
    `Detalhes da Alocação:\n\nCampanha: ${alocacao.campanha.titulo}\nTítulo: ${
      alocacao.tituloAlocacao
    }\nDescrição: ${
      alocacao.descricaoAlocacao || "Nenhuma descrição"
    }\nValor: R$ ${formatarValor(alocacao.valorAlocado)}\nData: ${formatarData(
      alocacao.dataAlocacao,
    )}\nResponsável: ${alocacao.responsavel.nome}`,
  );
}

async function excluirAlocacao(alocacao) {
  if (
    confirm(
      `Tem certeza que deseja excluir a alocação "${alocacao.tituloAlocacao}"?`,
    )
  ) {
    try {
      await axios.delete(
        `${process.env.VUE_APP_API_BASE_URL}/api/transparencia/${alocacao.id}`,
      );

      await carregarAlocacoes();

      alert("Alocação excluída com sucesso!");
    } catch (error) {
      console.error("Erro ao excluir alocação:", error);
      alert("Erro ao excluir alocação. Por favor, tente novamente.");
    }
  }
}
function fecharModalAlocacao() {
  showModalAlocacao.value = false;
  alocacaoSelecionada.value = {
    campanha: null,
    tituloAlocacao: "",
    descricaoAlocacao: "",
    valorAlocado: 0,
    comprovante: "",
  };
}

function novoUsuario() {
  Object.assign(usuarioSelecionado, {
    nome: "",
    email: "",
    senha: "",
    confirmarSenha: "",
  });
  showModalUsuario.value = true;
}

async function salvarUsuario() {
  if (!senhasIguais) {
    alert("As senhas não coincidem.");
    return;
  }

  salvandoUsuario.value = true;

  try {
    await axios.post(
      `${process.env.VUE_APP_API_BASE_URL}/api/auth/cadastro-admin`,
      {
        nome: usuarioSelecionado.nome,
        email: usuarioSelecionado.email,
        senha: usuarioSelecionado.senha,
      },
    );

    await carregarUsuarios();
    fecharModalUsuario();

    alert("Administrador criado com sucesso!");
  } catch (error) {
    console.error("Erro ao criar administrador:", error);
    alert("Erro ao criar administrador. Por favor, tente novamente.");
  } finally {
    salvandoUsuario.value = false;
  }
}

function verUsuario(usuario) {
  alert(
    `Detalhes do Usuário:\n\nNome: ${usuario.nome}\nEmail: ${
      usuario.email
    }\nTipo: ${usuario.tipo}\nData de Cadastro: ${formatarData(
      usuario.dataCadastro,
    )}\nStatus: ${usuario.ativo ? "Ativo" : "Inativo"}`,
  );
}

async function alterarStatus(usuario) {
  const novoStatus = usuario.ativo ? "inativar" : "ativar";
  if (
    confirm(`Tem certeza que deseja ${novoStatus} o usuário "${usuario.nome}"?`)
  ) {
    try {
      await axios.put(
        `${process.env.VUE_APP_API_BASE_URL}/api/usuarios/${usuario.id}/status`,
        {
          ativo: !usuario.ativo,
        },
      );

      await carregarUsuarios();

      alert(
        `Usuário ${
          novoStatus === "ativar" ? "ativado" : "inativado"
        } com sucesso!`,
      );
    } catch (error) {
      console.error(`Erro ao ${novoStatus} usuário:`, error);
      alert(`Erro ao ${novoStatus} usuário. Por favor, tente novamente.`);
    }
  }
}

function fecharModalUsuario() {
  showModalUsuario.value = false;
  Object.assign(usuarioSelecionado, {
    nome: "",
    email: "",
    senha: "",
    confirmarSenha: "",
  });
}

function formatarDataInput(data) {
  if (!data) return "";

  const year = data.getFullYear();
  const month = String(data.getMonth() + 1).padStart(2, "0");
  const day = String(data.getDate()).padStart(2, "0");
  console.log("formatarDataInput", data, `${year}-${month}-${day}`);
  return `${year}-${month}-${day}`;
}
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

.modal {
  transition: all 0.3s ease;
}

.card {
  transition: transform 0.2s;
}

.card:hover {
  transform: translateY(-5px);
}
</style>
