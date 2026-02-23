# Requisitos Detalhados - Sistema Web de Doações

## Entidades do Sistema

### 1. Usuário
- **Atributos**:
  - ID (identificador único)
  - Nome completo
  - Email (usado para login)
  - Senha (armazenada com criptografia)
  - Tipo (doador ou administrador)
  - Data de cadastro
  - Endereço (opcional para doadores, obrigatório para administradores)
  - Telefone (opcional)
  - CPF/CNPJ (opcional para doadores, obrigatório para administradores)
  - Status (ativo/inativo)

- **Funcionalidades**:
  - Cadastro de novo usuário (auto-registro como doador)
  - Login/Logout
  - Recuperação de senha
  - Visualização e edição de perfil
  - Histórico de doações (para doadores)
  - Gerenciamento de campanhas (para administradores)

### 2. Campanha
- **Atributos**:
  - ID (identificador único)
  - Título
  - Descrição detalhada
  - Imagem de capa
  - Meta financeira
  - Valor arrecadado
  - Data de início
  - Data de término
  - Status (ativa, finalizada, cancelada)
  - Categoria (ex: saúde, educação, emergência)
  - Administrador responsável
  - Beneficiários
  - Galeria de imagens (opcional)

- **Funcionalidades**:
  - Criação de campanha (apenas administradores)
  - Edição de campanha (apenas administradores)
  - Listagem de campanhas (todos os usuários)
  - Visualização detalhada de campanha (todos os usuários)
  - Finalização/cancelamento de campanha (apenas administradores)
  - Relatório de arrecadação

### 3. Doação
- **Atributos**:
  - ID (identificador único)
  - Campanha associada
  - Doador (usuário ou anônimo)
  - Valor
  - Data e hora
  - Forma de pagamento (simulado)
  - Status (confirmada, pendente, cancelada)
  - Mensagem de apoio (opcional)
  - Comprovante (opcional)

- **Funcionalidades**:
  - Realização de doação (usuários logados ou anônimos)
  - Emissão de comprovante
  - Cancelamento de doação (apenas administradores)
  - Listagem de doações por campanha (administradores)
  - Listagem de doações por usuário (doador vê apenas as próprias)

### 4. Transparência
- **Atributos**:
  - ID (identificador único)
  - Campanha associada
  - Título da alocação
  - Descrição da alocação
  - Valor alocado
  - Data da alocação
  - Comprovante (imagem/PDF)
  - Responsável pela alocação (administrador)

- **Funcionalidades**:
  - Registro de alocação de recursos (apenas administradores)
  - Visualização de alocações por campanha (todos os usuários)
  - Geração de relatórios de transparência (todos os usuários)
  - Upload de comprovantes (apenas administradores)

## Regras de Acesso

### Usuários Anônimos (não logados)
- Podem visualizar o portal de campanhas
- Podem visualizar a página de transparência
- Podem realizar doações anônimas
- Podem se cadastrar como doadores

### Doadores (usuários logados)
- Todas as permissões de usuários anônimos
- Podem visualizar e editar seu próprio perfil
- Podem visualizar seu histórico de doações
- Podem realizar doações identificadas
- Podem receber comprovantes de doação por email

### Administradores
- Todas as permissões de doadores
- Podem criar, editar e finalizar campanhas
- Podem visualizar todas as doações de todas as campanhas
- Podem registrar alocações de recursos na página de transparência
- Podem gerenciar usuários (ativar/desativar)
- Podem gerar relatórios gerais do sistema

## Fluxos Principais

### 1. Cadastro de Usuário
1. Usuário acessa a página de cadastro
2. Preenche formulário com dados pessoais
3. Sistema valida os dados (email único, senha forte)
4. Sistema envia email de confirmação
5. Usuário confirma email e conta é ativada
6. Usuário é redirecionado para login

### 2. Login
1. Usuário acessa página de login
2. Insere email e senha
3. Sistema valida credenciais
4. Sistema redireciona para página inicial com acesso às funcionalidades de acordo com o tipo de usuário

### 3. Visualização de Campanhas
1. Usuário acessa o portal de campanhas
2. Sistema exibe lista de campanhas ativas
3. Usuário pode filtrar por categoria ou status
4. Usuário pode clicar em uma campanha para ver detalhes
5. Sistema exibe página detalhada da campanha com informações e opção de doação

### 4. Realização de Doação
1. Usuário seleciona campanha para doar
2. Usuário preenche formulário de doação (valor, forma de pagamento, mensagem opcional)
3. Sistema processa doação (simulado)
4. Sistema confirma doação e atualiza valor arrecadado da campanha
5. Sistema gera comprovante de doação

### 5. Visualização de Transparência
1. Usuário acessa página de transparência
2. Usuário seleciona campanha para visualizar alocações
3. Sistema exibe lista de alocações com valores, datas e comprovantes
4. Usuário pode baixar relatório de transparência

### 6. Gerenciamento de Campanha (Administradores)
1. Administrador acessa área de gerenciamento
2. Cria nova campanha ou seleciona campanha existente para editar
3. Preenche/edita informações da campanha
4. Sistema valida e salva alterações
5. Campanha é publicada/atualizada no portal

### 7. Registro de Alocação (Administradores)
1. Administrador acessa área de transparência
2. Seleciona campanha para registrar alocação
3. Preenche formulário de alocação (título, descrição, valor, comprovante)
4. Sistema valida e salva alocação
5. Alocação é publicada na página de transparência

## Requisitos Não-Funcionais

### Segurança
- Senhas armazenadas com hash e salt
- Autenticação via JWT (JSON Web Token)
- Proteção contra ataques CSRF
- Validação de entrada em todos os formulários
- HTTPS para todas as comunicações

### Performance
- Tempo de resposta máximo de 2 segundos para operações comuns
- Suporte a pelo menos 100 usuários simultâneos
- Otimização de imagens para carregamento rápido

### Usabilidade
- Interface responsiva (desktop e mobile)
- Acessibilidade (WCAG 2.1 nível AA)
- Feedback visual para todas as ações do usuário
- Mensagens de erro claras e informativas

### Tecnologias
- Backend: Java Spring Boot
- Banco de dados: PostgreSQL
- Frontend: Vue.js com hashHistory
- API RESTful para comunicação entre frontend e backend

## Integrações

### Backend-Frontend
- API RESTful com endpoints documentados
- Autenticação via JWT
- Comunicação assíncrona para operações demoradas
- Validação de dados tanto no frontend quanto no backend

### Simulação de Pagamento
- Interface para simular diferentes formas de pagamento
- Confirmação automática de doações (em ambiente real seria integrado com gateway de pagamento)

### Envio de Email
- Confirmação de cadastro
- Recuperação de senha
- Comprovante de doação
- Notificações de campanhas (opcional)
