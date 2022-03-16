create table if not exists fornecedor (
  id bigserial not null,
  ativo boolean not null default false,
  cpf_cnpj varchar(14) not null,
  nome varchar(200) not null,
  telefone varchar(12) not null,
  tenant varchar(50) not null,
  versao_objeto int4 not null,
  CONSTRAINT fornecedor_pkey PRIMARY KEY (id)
);