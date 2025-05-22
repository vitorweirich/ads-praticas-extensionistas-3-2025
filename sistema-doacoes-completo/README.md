# Documentação do Sistema Web de Doações

## Visão Geral

Este sistema web de doações foi desenvolvido para apoiar a comunidade, permitindo que usuários façam doações para campanhas específicas e acompanhem a alocação dos recursos através de uma página de transparência. O sistema possui uma arquitetura cliente-servidor, com backend em Java Spring Boot, banco de dados PostgreSQL e frontend em Vue.js.

## Estrutura do Projeto

```
sistema-doacoes/
├── backend/                 # Aplicação Spring Boot
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/doacoes/api/
│   │   │   │   ├── controller/       # Controladores REST
│   │   │   │   ├── model/            # Entidades JPA
│   │   │   │   ├── repository/       # Repositórios Spring Data
│   │   │   │   ├── security/         # Configuração de segurança e JWT
│   │   │   │   ├── payload/          # DTOs para requisições/respostas
│   │   │   │   └── ApiApplication.java
│   │   │   └── resources/
│   │   │       └── application.properties
│   └── pom.xml              # Dependências Maven
│
├── frontend/                # Aplicação Vue.js
│   ├── public/
│   │   └── index.html
│   ├── src/
│   │   ├── assets/          # Recursos estáticos
│   │   ├── components/      # Componentes Vue reutilizáveis
│   │   ├── views/           # Páginas da aplicação
│   │   ├── router/          # Configuração de rotas
│   │   ├── store/           # Gerenciamento de estado (Vuex)
│   │   ├── services/        # Serviços para API
│   │   ├── App.vue          # Componente raiz
│   │   └── main.js          # Ponto de entrada
│   ├── .gitignore          # Arquivos que ignorados pelo git
│   ├── vue.config.js       # Configurações do Vue
│   ├── .gitignore          # Arquivos que ignorados pelo git
│   └── package.json         # Dependências NPM
│
│
└── README.md                # Este arquivo
```

## Requisitos de Sistema

### Backend

- Java 11 ou superior
- Maven 3.6 ou superior
- PostgreSQL 12 ou superior

### Frontend

- Node.js 14 ou superior
- NPM 6 ou superior

## Instalação e Configuração

### Banco de Dados PostgreSQL

1. Instale o PostgreSQL seguindo as instruções para seu sistema operacional em [postgresql.org](https://www.postgresql.org/download/)

2. Crie um banco de dados para o sistema:

```sql
CREATE DATABASE doacoes_db;
CREATE USER postgres WITH ENCRYPTED PASSWORD 'postgres';
GRANT ALL PRIVILEGES ON DATABASE doacoes_db TO postgres;
```

### Backend (Spring Boot)

1. Navegue até a pasta do backend:

```bash
cd backend
```

2. Configure o banco de dados no arquivo `src/main/resources/application.properties` (já está configurado com valores padrão):

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/doacoes_db
spring.datasource.username=postgres
spring.datasource.password=postgres
```

3. Compile e execute o backend:

```bash
mvn clean install
mvn spring-boot:run
```

O backend estará disponível em `http://localhost:8080`

### Frontend (Vue.js)

1. Navegue até a pasta do frontend:

```bash
cd frontend
```

2. Instale as dependências:

```bash
yarn
```

3. Configure a URL da API no arquivo `src/services/api.js` (se necessário):

```javascript
// Já está configurado para apontar para http://localhost:8080
```

4. Execute o frontend em modo de desenvolvimento:

```bash
yarn serve
```

O frontend estará disponível em `http://localhost:3000`

5. Para gerar a versão de produção:

```bash
yarn build
```

Os arquivos estáticos serão gerados na pasta `dist/` e podem ser servidos por qualquer servidor web.

## Funcionalidades Principais

### 1. Cadastro e Autenticação

- **Cadastro de Usuários**: Novos usuários podem se cadastrar como doadores através da página de cadastro.
- **Login**: Usuários cadastrados podem fazer login para acessar funcionalidades específicas.
- **Perfil**: Usuários podem visualizar e editar suas informações pessoais.

### 2. Portal de Campanhas

- **Listagem de Campanhas**: Visualização de todas as campanhas ativas.
- **Filtros**: Possibilidade de filtrar campanhas por categoria.
- **Detalhes**: Visualização detalhada de cada campanha, incluindo descrição, meta e valor arrecadado.

### 3. Doações

- **Realizar Doação**: Usuários podem fazer doações para campanhas específicas.
- **Histórico**: Doadores podem visualizar seu histórico de doações.
- **Comprovantes**: Geração de comprovantes para cada doação realizada.

### 4. Transparência

- **Alocação de Recursos**: Visualização de como os recursos de cada campanha estão sendo utilizados.
- **Comprovantes**: Acesso a comprovantes de alocação de recursos.
- **Filtros**: Possibilidade de filtrar alocações por campanha.

### 5. Administração

- **Gerenciamento de Campanhas**: Administradores podem criar, editar e finalizar campanhas.
- **Gerenciamento de Doações**: Confirmação e cancelamento de doações.
- **Registro de Alocações**: Registro de como os recursos estão sendo utilizados.
- **Gerenciamento de Usuários**: Ativação/desativação de contas de usuários.

## Usuários Padrão

Para facilitar os testes, o sistema já vem com alguns usuários pré-cadastrados:

// TODO: Adicionar flyway e seed de usuarios padrão?

1. **Administrador**

   - Email: admin@example.com
   - Senha: admin123

2. **Doador**
   - Email: doador@example.com
   - Senha: doador123

## API REST

O backend expõe uma API REST com os seguintes endpoints principais:

### Autenticação

- `POST /api/auth/login`: Autenticação de usuário
- `POST /api/auth/cadastro`: Cadastro de novo usuário

### Campanhas

- `GET /api/campanhas/publicas`: Lista todas as campanhas públicas
- `GET /api/campanhas/{id}`: Obtém detalhes de uma campanha específica
- `POST /api/campanhas`: Cria uma nova campanha (requer autenticação de administrador)
- `PUT /api/campanhas/{id}`: Atualiza uma campanha existente (requer autenticação de administrador)

### Doações

- `POST /api/doacoes`: Registra uma nova doação
- `GET /api/doacoes/usuario/{id}`: Lista doações de um usuário específico
- `GET /api/doacoes/campanha/{id}`: Lista doações para uma campanha específica

### Transparência

- `GET /api/transparencia/publica`: Lista todas as alocações públicas
- `GET /api/transparencia/campanha/{id}`: Lista alocações para uma campanha específica
- `POST /api/transparencia`: Registra uma nova alocação (requer autenticação de administrador)

## Segurança

O sistema utiliza JWT (JSON Web Token) para autenticação e autorização. Cada requisição autenticada deve incluir um token no cabeçalho:

```
Authorization: Bearer {token}
```

O token é obtido através do endpoint de login e tem validade de 24 horas.
