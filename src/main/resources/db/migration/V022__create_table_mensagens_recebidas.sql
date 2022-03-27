create table if not exists mensagens_recebidas(
  id serial not null primary key unique,
  remetente varchar(255) not null,
  destinatario varchar(255) not null,
  mensagem varchar(255) not null,
  lida boolean not null default false,
  notificado boolean not null default false,
  data_hora_mensagem_recebida timestamp not null,
  usuario_remetente_id int8 not null,
  CONSTRAINT fkey_usuario_remetente FOREIGN KEY (usuario_remetente_id) REFERENCES usuario(id)
);