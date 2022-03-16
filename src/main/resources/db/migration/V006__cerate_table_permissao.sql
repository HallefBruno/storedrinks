create table if not exists permissao (
  id bigserial not null,
  nome varchar(255) null,
  CONSTRAINT permissao_pkey PRIMARY KEY (id)
);