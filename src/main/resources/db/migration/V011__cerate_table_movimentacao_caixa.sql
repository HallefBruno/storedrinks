create table if not exists movimentacao_caixa (
  id bigserial not null,
  forma_pagamento varchar(255) not null,
  tenant varchar(20) not null,
  valor_entrada numeric(19, 2) not null,
  valor_saida numeric(19, 2) not null,
  abrir_caixa int8 not null,
  venda_id int8 not null,
  CONSTRAINT movimentacao_caixa_pkey PRIMARY KEY (id),
  CONSTRAINT movimentacao_caixa_valor_entrada_check CHECK ((valor_entrada >= (0)::numeric)),
  CONSTRAINT movimentacao_caixa_valor_saida_check CHECK ((valor_saida >= (0)::numeric)),
  CONSTRAINT fk8yr170vxwc1v3lwfyg74tb38v FOREIGN KEY (abrir_caixa) REFERENCES abrir_caixa(id),
  CONSTRAINT fkkutmoof2jeqos8fvuuo4bjk06 FOREIGN KEY (venda_id) REFERENCES venda(id)
);