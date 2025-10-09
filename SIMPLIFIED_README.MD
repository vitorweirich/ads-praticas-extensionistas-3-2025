# SIMPLIFIED_README.MD

## Propósito do Repositório

Este repositório contém o **Sistema Web de Doações**, desenvolvido para apoiar campanhas sociais, permitindo cadastro de usuários (doadores e administradores), divulgação de campanhas, realização de doações e acompanhamento transparente da alocação dos recursos.

### Principais Funcionalidades

- Cadastro e autenticação de usuários
- Portal público de campanhas
- Página de transparência das doações
- Gerenciamento de campanhas (admin)
- Relatórios e histórico de doações

### Arquitetura

- **Backend:** Java Spring Boot (API REST)
- **Frontend:** Vue.js (SPA)
- **Banco de Dados:** PostgreSQL
- **Infraestrutura:** Docker, Docker Compose, Nginx, Coolify/Dokploy (self-host em VPS)

## Estrutura do Projeto

- `sistema-doacoes-completo/backend/` — Código fonte do backend (Spring Boot)
- `sistema-doacoes-completo/frontend/` — Código fonte do frontend (Vue.js)
- `modelagem-banco-de-dados/` — Scripts SQL e diagramas do banco de dados
- `diagramas/` — Diagramas UML, arquitetura, DevOps e casos de uso
- `DEPLOY.MD` — Guia de deploy detalhado usando Coolify em VPS
- `PUBLIC_API.md` — Documentação do endpoint público de campanhas
- `praticas-IV/README.MD` — Documentação acadêmica e justificativas de arquitetura
- `sistema-doacoes-completo/requisitos.md` — Requisitos detalhados do sistema

## Referências Rápidas

- **Como rodar o sistema:** Veja `sistema-doacoes-completo/README.md`
- **Deploy em produção:** Siga o passo a passo em `DEPLOY.MD`
- **API pública de campanhas:** Detalhes em `PUBLIC_API.md`
- **Requisitos e regras de negócio:** Consulte `sistema-doacoes-completo/requisitos.md`
- **Diagramas e arquitetura:** Arquivos `.puml` e imagens em `diagramas/` e `praticas-IV/README.MD`

## Links Úteis

- [Repositório GitHub](https://github.com/vitorweirich/ads-praticas-extensionistas-3-2025)
- Documentações acadêmicas: [Praticas III](README.MD) e [Praticas IV](praticas-IV/README.MD)
