create table if not exists cliente_sistema(
  id bigserial not null primary key unique,
  nome_comercio varchar(255) not null,
  cpf_cnpj varchar(14) not null unique,
  tenant varchar(50) not null unique,
  data_cadastro timestamp not null,
  data_atualizacao timestamp not null,
  estado varchar(100) not null,
  cidade varchar(200) not null,
  bairro varchar(200) not null,
  cep varchar(10) not null,
  logradouro varchar(100) not null,
  qtd_usuario int8 not null,
  acessar_tela_criar_login boolean null default false,
  primeiro_acesso boolean null default false
);