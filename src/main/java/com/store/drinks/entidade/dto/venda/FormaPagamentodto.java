package com.store.drinks.entidade.dto.venda;

import com.store.drinks.entidade.enuns.TipoPagamento;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FormaPagamentodto implements Serializable {
  private TipoPagamento tipoPagamento;
  private BigDecimal valor;
}
