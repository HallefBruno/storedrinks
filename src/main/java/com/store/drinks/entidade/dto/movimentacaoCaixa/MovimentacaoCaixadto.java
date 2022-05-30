package com.store.drinks.entidade.dto.movimentacaoCaixa;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovimentacaoCaixadto implements Serializable {
  
  private Long movimentacaoId;
  private Long usuarioId;
  private Long vendaId;
  private Long caixaId;
  
  private String nomeVendedor;
  private Boolean recolhimento;
  private String tenant;
  private BigDecimal valorRecebido;
  private BigDecimal valorTroco;
  private LocalDateTime dataMovimentacao;
  
  private BigDecimal somaValorTotalSaida;
  private BigDecimal somaValorTotal;

  
}