create table if not exists itens_venda (
  id bigserial not null,
  quantidade int4 not null,
  tenant varchar(20) not null,
  produto_id int8 not null,
  venda_id int8 not null,
  CONSTRAINT itens_venda_pkey PRIMARY KEY (id),
  CONSTRAINT itens_venda_quantidade_check CHECK ((quantidade >= 1)),
  CONSTRAINT fke4xxiagi5h313j3liwttp5g9h FOREIGN KEY (produto_id) REFERENCES produto(id),
  CONSTRAINT fkfvivokqub75o9uctx9g1ipgab FOREIGN KEY (venda_id) REFERENCES venda(id)
);
