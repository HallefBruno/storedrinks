package com.store.drinks.entidade.dto.movimentacaoCaixa;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
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
  
  private BigDecimal somaValorTotalSaida;
  private BigDecimal somaValorTotal;

  public Long getMovimentacaoId() {
    return movimentacaoId;
  }

  public void setMovimentacaoId(Long movimentacaoId) {
    this.movimentacaoId = movimentacaoId;
  }

  public Long getUsuarioId() {
    return usuarioId;
  }

  public void setUsuarioId(Long usuarioId) {
    this.usuarioId = usuarioId;
  }

  public Long getVendaId() {
    return vendaId;
  }

  public void setVendaId(Long vendaId) {
    this.vendaId = vendaId;
  }

  public Long getCaixaId() {
    return caixaId;
  }

  public void setCaixaId(Long caixaId) {
    this.caixaId = caixaId;
  }

  public Boolean getRecolhimento() {
    return recolhimento;
  }

  public void setRecolhimento(Boolean recolhimento) {
    this.recolhimento = recolhimento;
  }

  public String getTenant() {
    return tenant;
  }

  public void setTenant(String tenant) {
    this.tenant = tenant;
  }

  public BigDecimal getValorRecebido() {
    return valorRecebido;
  }

  public void setValorRecebido(BigDecimal valorRecebido) {
    this.valorRecebido = valorRecebido;
  }

  public BigDecimal getValorTroco() {
    return valorTroco;
  }

  public void setValorTroco(BigDecimal valorTroco) {
    this.valorTroco = valorTroco;
  }

  public LocalDateTime getDataMovimentacao() {
    return dataMovimentacao;
  }

  public void setDataMovimentacao(LocalDateTime dataMovimentacao) {
    this.dataMovimentacao = dataMovimentacao;
  }

  public BigDecimal getSomaValorTotalSaida() {
    return somaValorTotalSaida;
  }

  public void setSomaValorTotalSaida(BigDecimal somaValorTotalSaida) {
    this.somaValorTotalSaida = somaValorTotalSaida;
  }

  public BigDecimal getSomaValorTotal() {
    return somaValorTotal;
  }

  public void setSomaValorTotal(BigDecimal somaValorTotal) {
    this.somaValorTotal = somaValorTotal;
  }
  
  
  
}
