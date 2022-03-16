create table if not exists produto(
  id bigserial not null primary key unique,
  codigo_barra varchar(30) not null,
  codigo_produto varchar(30) not null,
  descricao_produto varchar(255) not null,
  quantidade integer not null,
  valor_custo numeric(19, 2) not null,
  valor_venda numeric(19, 2) not null,
  ativo bool null default false,
  versao_objeto int4 not null,
  tenant varchar(50) not null,
  CONSTRAINT produto_quantidade_check CHECK ((quantidade >= 0)),
  CONSTRAINT produto_valor_custo_check CHECK ((valor_custo >= (0)::numeric)),
  CONSTRAINT produto_valor_venda_check CHECK ((valor_venda >= (0)::numeric))
);