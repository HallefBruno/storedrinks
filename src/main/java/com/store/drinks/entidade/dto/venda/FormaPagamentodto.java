package com.store.drinks.entidade.dto.venda;

import com.store.drinks.entidade.enuns.FormaPagamento;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FormaPagamentodto {

  private FormaPagamento formaPagamento;
  private BigDecimal valor;
}
