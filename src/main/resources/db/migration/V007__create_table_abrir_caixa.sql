create table if not exists abrir_caixa (
  id bigserial not null,
  aberto bool not null,
  data_hora_abertura timestamp not null,
  data_hora_fechamento timestamp NULL,
  tenant varchar(50) not null,
  valor_inicial_troco numeric(19, 2) not null,
  usuario_id int8 not null,
  CONSTRAINT abrir_caixa_pkey PRIMARY KEY (id),
  CONSTRAINT abrir_caixa_valor_inicial_troco_check CHECK ((valor_inicial_troco >= (0)::numeric)),
  CONSTRAINT fk8dcodbw1y358qj4techd92cb1 FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);

