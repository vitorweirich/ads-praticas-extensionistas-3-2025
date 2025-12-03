# API Pública de Campanhas

Este documento descreve o endpoint público `https://doacoes-api.vitorweirich.com/api/campanhas/publicas`, que pode ser utilizado para integrar e exibir campanhas principais em sistemas externos, como portais, sites institucionais ou aplicativos de terceiros.

> **Disclaimer:** Apesar do sistema completo ser construído sobre APIs REST, **não é recomendado o uso dos demais endpoints** para integrações externas, pois eles exigem autenticação e são voltados para o funcionamento interno do sistema de doações.

## Endpoint: Listar Campanhas Públicas

- **URL:** `https://doacoes-api.vitorweirich.com/api/campanhas/publicas`
- **Método:** `GET`
- **Autenticação:** Não requer autenticação
- **Descrição:** Retorna uma lista das campanhas públicas ativas, permitindo que sistemas externos exibam informações das principais campanhas.

### Exemplo de Retorno

```json
[
  {
    "id": 5,
    "titulo": "Campanha do Livro",
    "descricao": "Campanha “Doe uma História, Inspire um Futuro”",
    "imagemCapa": "https://minio.vitorweirich.com/praticas-extensionistas/capanha-livros-grande.jpg",
    "metaFinanceira": 35000,
    "valorArrecadado": 240,
    "dataTermino": "2025-10-31",
    "status": "ATIVA",
    "categoria": "educacao",
    "beneficiarios": "Escola Básica do Brasil"
  },
  {
    "id": 7,
    "titulo": "Teste",
    "descricao": "Teste",
    "imagemCapa": "https://www.bandeirantes.pr.gov.br/public/admin/globalarq/uploads/files/Junho%20Verde%202024/Cartaz%20Semana%20do%20Meio%20Ambiente.png",
    "metaFinanceira": 123,
    "valorArrecadado": 150,
    "dataTermino": "2025-08-08",
    "status": "ATIVA",
    "categoria": "ambiental",
    "beneficiarios": "test"
  }
  // ... outros objetos de campanha
]
```

### Campos do Objeto Campanha

- `id`: Identificador único da campanha
- `titulo`: Título da campanha
- `descricao`: Descrição detalhada
- `imagemCapa`: URL da imagem de capa
- `metaFinanceira`: Meta financeira da campanha
- `valorArrecadado`: Valor já arrecadado
- `dataTermino`: Data de término (formato YYYY-MM-DD)
- `status`: Status da campanha (ex: ATIVA)
- `categoria`: Categoria da campanha (ex: educacao, ambiental, saude)
- `beneficiarios`: Instituições ou pessoas beneficiadas

## Recomendações de Uso

- Utilize este endpoint apenas para consulta e exibição pública das campanhas.
- Não utilize os demais endpoints da API para integrações externas, pois exigem autenticação e podem sofrer alterações sem aviso prévio.
