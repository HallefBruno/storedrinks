package com.store.drinks.entidade.dto.movimentacaoCaixa;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovimentacaoCaixadto implements Serializable {
  
  private Long movimentacaoId;
  private Long usuarioId;
  private Long vendaId;
  private Long caixaId;
  
  private Boolean recolhimento;
  private String tenant;
  private BigDecimal valorRecebido;
  private BigDecimal valorTroco;
  private LocalDateTime dataMovimentacao;
  
}
