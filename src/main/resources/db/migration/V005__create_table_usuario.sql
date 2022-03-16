create table if not exists usuario (
  id bigserial not null primary key unique,
  ativo bool not null,
  data_nascimento date null,
  email varchar(255) null unique,
  nome varchar(255) null,
  proprietario bool not null,
  senha varchar(255) not null,
  tenant varchar(50) not null,
  CONSTRAINT fkdfbn4tm8qot0lp4peoh64gon4 FOREIGN KEY (tenant) REFERENCES cliente_sistema(tenant)
);
