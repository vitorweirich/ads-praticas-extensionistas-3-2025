CREATE TABLE usuarios (
	id bigserial PRIMARY KEY,
	ativo bool NOT NULL,
	cpf_cnpj varchar(255),
	data_cadastro timestamp,
	email varchar(100),
	endereco varchar(255),
	nome varchar(100),
	senha varchar(120),
	telefone varchar(255),
	tipo varchar(255),
	UNIQUE (email)
);

CREATE TABLE campanhas (
	id bigserial PRIMARY KEY,
	beneficiarios varchar(255),
	categoria varchar(50),
	data_termino date,
	descricao text,
	imagem_capa varchar(255),
	meta_financeira numeric(19, 2) NOT NULL,
	status varchar(255),
	titulo varchar(100),
	valor_arrecadado numeric(19, 2),
	administrador_id int8,
	FOREIGN KEY (administrador_id) REFERENCES usuarios(id)
);

CREATE TABLE doacoes (
	id bigserial PRIMARY KEY,
	anonimo bool NOT NULL,
	data_hora timestamp,
	forma_pagamento varchar(255),
	mensagem_apoio varchar(255),
	status varchar(255),
	valor numeric(19, 2) NOT NULL,
	campanha_id int8,
	doador_id int8,
	FOREIGN KEY (doador_id) REFERENCES usuarios(id),
	FOREIGN KEY (campanha_id) REFERENCES campanhas(id)
);

CREATE TABLE transparencia (
	id bigserial PRIMARY KEY,
	comprovante varchar(255),
	data_alocacao timestamp,
	descricao_alocacao text,
	titulo_alocacao varchar(255),
	valor_alocado numeric(19, 2) NOT NULL,
	campanha_id int8,
	responsavel_id int8,
	FOREIGN KEY (campanha_id) REFERENCES campanhas(id),
	FOREIGN KEY (responsavel_id) REFERENCES usuarios(id)
);