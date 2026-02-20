-- Inserção de usuários
INSERT INTO
  usuarios (
    ativo,
    cpf_cnpj,
    data_cadastro,
    email,
    endereco,
    nome,
    senha,
    telefone,
    tipo
  )
VALUES
  (
    true,
    '12345678901',
    '2025-07-01 10:00:00',
    'joao@email.com',
    'Rua A, 123',
    'João Silva',
    '$2a$10$aeKHMyGojYjAs8NoMudg4OMaOcYb4BS8nhUEEl7YLtghuBGzVTvOy',
    '11999999999',
    'ADMINISTRADOR'
  ),
  (
    true,
    '98765432100',
    '2025-07-02 11:00:00',
    'maria@email.com',
    'Rua B, 456',
    'Maria Souza',
    '$2a$10$aeKHMyGojYjAs8NoMudg4OMaOcYb4BS8nhUEEl7YLtghuBGzVTvOy',
    '11999999997',
    'DOADOR'
  ),
  (
    true,
    '11122233344',
    '2025-07-03 12:00:00',
    'carlos@email.com',
    'Rua C, 789',
    'Carlos Lima',
    '$2a$10$aeKHMyGojYjAs8NoMudg4OMaOcYb4BS8nhUEEl7YLtghuBGzVTvOy',
    '11999999998',
    'DOADOR'
  );

-- Inserção de campanhas
INSERT INTO
  campanhas (
    beneficiarios,
    categoria,
    data_termino,
    descricao,
    imagem_capa,
    meta_financeira,
    status,
    titulo,
    valor_arrecadado,
    administrador_id
  )
VALUES
  (
    'Crianças',
    'saude',
    '2025-12-31',
    'Campanha para ajudar crianças',
    'https://minio.vitorweirich.com/praticas-extensionistas/campanha-castrar-final.png',
    10000.00,
    'ATIVA',
    'Ajude as Crianças',
    2500.00,
    1
  ),
  (
    'Animais',
    'ambiental',
    '2025-10-15',
    'Campanha para animais abandonados',
    'https://minio.vitorweirich.com/praticas-extensionistas/capanha-livros-grande.jpg',
    5000.00,
    'ATIVA',
    'Proteja os Animais',
    1200.00,
    2
  );

-- Inserção de doações
INSERT INTO
  doacoes (
    anonimo,
    data_hora,
    forma_pagamento,
    mensagem_apoio,
    status,
    valor,
    campanha_id,
    doador_id
  )
VALUES
  (
    false,
    '2025-07-05 14:00:00',
    'PIX',
    'Boa sorte!',
    'CONFIRMADA',
    100.00,
    1,
    2
  ),
  (
    true,
    '2025-07-06 15:30:00',
    'CARTAO_CREDITO',
    'Ajudando!',
    'CONFIRMADA',
    200.00,
    1,
    3
  ),
  (
    true,
    '2025-07-06 15:30:00',
    'TRANSFERENCIA',
    'Espero que ajude',
    'CONFIRMADA',
    175.00,
    1,
    3
  ),
  (
    false,
    '2025-07-07 16:45:00',
    'BOLETO',
    'Força!',
    'PENDENTE',
    50.00,
    2,
    1
  );

-- Inserção de transparência
INSERT INTO
  transparencia (
    comprovante,
    data_alocacao,
    descricao_alocacao,
    titulo_alocacao,
    valor_alocado,
    campanha_id,
    responsavel_id
  )
VALUES
  (
    'https://minio.vitorweirich.com/praticas-extensionistas/comprov-transparencia-mock.png',
    '2025-07-08 09:00:00',
    'Compra de materiais',
    'Materiais Escolares',
    500.00,
    1,
    1
  ),
  (
    'https://minio.vitorweirich.com/praticas-extensionistas/comprov-transparencia-mock.png',
    '2025-07-08 10:00:00',
    'Vacinas para animais',
    'Vacinação',
    300.00,
    2,
    2
  );