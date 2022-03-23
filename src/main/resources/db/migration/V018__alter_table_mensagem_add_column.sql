alter table mensagem
  add constraint fk_mensagem_usuario foreign key (usuario_id) references usuario (id);