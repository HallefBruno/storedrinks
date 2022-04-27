package com.store.drinks.entidade.dto.venda;

import com.store.drinks.entidade.MovimentacaoCaixa;
import com.store.drinks.entidade.enuns.TipoPagamento;
import com.store.drinks.repository.util.Multitenancy;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FormaPagamentodto {

  private Long id;
  private TipoPagamento tipoPagamento;
  private BigDecimal valor;
  private MovimentacaoCaixa movimentacaoCaixa;
  private String tenant;
  
  public FormaPagamentodto() {
    tenant = new Multitenancy().getTenantValue();
  }
  
}
