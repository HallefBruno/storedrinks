create table if not exists grupo_permissao (
  id_grupo int8 not null,
  id_permissao int8 not null,
  CONSTRAINT grupo_permissao_pkey PRIMARY KEY (id_grupo, id_permissao),
  CONSTRAINT fk9oesxnxaml2s24exucm3jr4fr FOREIGN KEY (id_grupo) REFERENCES grupo(id),
  CONSTRAINT fkfdid07dqkcx8cc53nj7rkmvcl FOREIGN KEY (id_permissao) REFERENCES permissao(id)
);
