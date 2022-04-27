package com.store.drinks.entidade.dto.venda;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItensVendadto {
  
  
  private Long id;
  private String codProduto;
  private String codigoBarra;
  private Integer quantidade;

}
