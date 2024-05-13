package com.store.drinks.entidade.dto.venda;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItensVendaCancelardto implements Serializable {
  
  private Long id;
  private Integer quantidade;
  private String descricaoProduto;
  private BigDecimal valorVenda;
  private BigDecimal valorTotalVenda;
  
}
