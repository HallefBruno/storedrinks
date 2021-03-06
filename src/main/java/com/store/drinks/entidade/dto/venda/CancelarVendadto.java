package com.store.drinks.entidade.dto.venda;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CancelarVendadto implements Serializable, Comparable<CancelarVendadto>{
  
  private Long movimentacaoCaixaId;
  private String tenant;
  private BigDecimal valorRecebido;
  private BigDecimal valorTroco;
  private Long caixaId;
  private Long vendaId;
  private BigDecimal valorTotalVenda;
  private LocalDateTime dataHoraVenda;
  private Long usuarioId;
  private String nome;
  private String email;
  
  @Override
  public int compareTo(CancelarVendadto cancelarVendadto) {
    return this.getMovimentacaoCaixaId().compareTo(cancelarVendadto.getMovimentacaoCaixaId());
  }
  
}
