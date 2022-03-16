create table if not exists venda(
  id bigserial not null primary key,
  data_hora_venda timestamp not null,
  tenant varchar(50) not null,
  valor_total_venda numeric(19, 2) not null,
  CONSTRAINT venda_valor_total_venda_check CHECK ((valor_total_venda >= (0)::numeric))
);