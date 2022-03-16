create table if not exists validar_cliente (
  id bigserial not null primary key,
  conta_criada bool null default false,
  cpf_cnpj varchar(14) not null unique,
  data_validacao timestamp not null
);