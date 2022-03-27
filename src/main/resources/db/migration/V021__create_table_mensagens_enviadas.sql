create table if not exists mensagens_enviadas(
  id serial not null primary key unique,
  remetente varchar(255) not null,
  destinatario varchar(255) not null,
  mensagem varchar(255) not null,
  data_hora_mensagem_enviada timestamp not null,
  usuario_destinatario_id int8 not null,
  CONSTRAINT fkey_usuario_destino FOREIGN KEY (usuario_destinatario_id) REFERENCES usuario(id)
);