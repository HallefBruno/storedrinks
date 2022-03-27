create table if not exists mensagem (
  id bigserial not null primary key unique,
  tenant varchar(50) not null,
  destinatario varchar(50) not null,
  mensagem varchar(255) not null,
  lida boolean not null default false
);