create table if not exists entrada_produto (
  id bigserial not null,
  data_emissao date not null,
  forma_pagamento varchar(80) not null,
  nova_quantidade int4 not null,
  novo_valor_custo numeric(19, 2) not null,
  novo_valor_venda numeric(19, 2) not null,
  numero_nota varchar(30) not null,
  qtd_parcelas int4 NULL,
  quantidade int4 not null,
  quantidade_incrementar_estoque int4 NULL,
  situacao_compra varchar(50) not null,
  tenant varchar(50) not null,
  valor_custo numeric(19, 2) not null,
  valor_total numeric(19, 2) not null,
  valor_venda numeric(19, 2) not null,
  versao_objeto int4 not null,
  fornecedor_id int8 not null,
  produto_id int8 not null,
  CONSTRAINT entrada_produto_nova_quantidade_check CHECK ((nova_quantidade >= 0)),
  CONSTRAINT entrada_produto_novo_valor_custo_check CHECK ((novo_valor_custo >= (0)::numeric)),
  CONSTRAINT entrada_produto_novo_valor_venda_check CHECK ((novo_valor_venda >= (0)::numeric)),
  CONSTRAINT entrada_produto_pkey PRIMARY KEY (id),
  CONSTRAINT entrada_produto_quantidade_check CHECK ((quantidade >= 0)),
  CONSTRAINT entrada_produto_valor_custo_check CHECK ((valor_custo >= (0)::numeric)),
  CONSTRAINT entrada_produto_valor_total_check CHECK ((valor_total >= (0)::numeric)),
  CONSTRAINT entrada_produto_valor_venda_check CHECK ((valor_venda >= (0)::numeric)),
  CONSTRAINT uk_h356b6p9d57giyxq7sd9uo5cy UNIQUE (numero_nota),
  CONSTRAINT fkf5altn6im28l2i6q81pm975mt FOREIGN KEY (produto_id) REFERENCES produto(id),
  CONSTRAINT fktr8nv0kvsoxh1ychoc4wpvijl FOREIGN KEY (fornecedor_id) REFERENCES fornecedor(id)
);