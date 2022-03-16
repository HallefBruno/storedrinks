create table if not exists usuario_grupo (
  id_usuario int8 not null,
  id_grupo int8 not null,
  CONSTRAINT usuario_grupo_pkey PRIMARY KEY (id_usuario, id_grupo),
  CONSTRAINT fk9huj1upwjyabwkwnpnhnernnu FOREIGN KEY (id_usuario) REFERENCES usuario(id),
  CONSTRAINT fkcu6om65mvqr6ct95ijgqgx7ww FOREIGN KEY (id_grupo) REFERENCES grupo(id)
);
