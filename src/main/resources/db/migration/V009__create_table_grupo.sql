create table if not exists grupo (
  id bigserial not null,
  nome varchar(255) not null,
  CONSTRAINT grupo_pkey PRIMARY KEY (id)
);