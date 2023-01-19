package com.store.drinks.entidade.dto.caixa;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Builder;

@Builder
public class DetalhesFechamentoCaixadto implements Serializable {
  
  private BigDecimal valorTotalEmVendasPorUsuario;
  private BigDecimal valorInicialTroco;
  private BigDecimal valorTotalSangria;

  public DetalhesFechamentoCaixadto() {
  }

  public DetalhesFechamentoCaixadto(BigDecimal valorTotalEmVendasPorUsuario, BigDecimal valorInicialTroco, BigDecimal valorTotalSangria) {
    this.valorTotalEmVendasPorUsuario = valorTotalEmVendasPorUsuario;
    this.valorInicialTroco = valorInicialTroco;
    this.valorTotalSangria = valorTotalSangria;
  }

  public BigDecimal getValorTotalEmVendasPorUsuario() {
    return valorTotalEmVendasPorUsuario;
  }

  public void setValorTotalEmVendasPorUsuario(BigDecimal valorTotalEmVendasPorUsuario) {
    this.valorTotalEmVendasPorUsuario = valorTotalEmVendasPorUsuario;
  }

  public BigDecimal getValorInicialTroco() {
    return valorInicialTroco;
  }

  public void setValorInicialTroco(BigDecimal valorInicialTroco) {
    this.valorInicialTroco = valorInicialTroco;
  }

  public BigDecimal getValorTotalSangria() {
    return valorTotalSangria;
  }

  public void setValorTotalSangria(BigDecimal valorTotalSangria) {
    this.valorTotalSangria = valorTotalSangria;
  }

  @Override
  public String toString() {
    return "DetalhesFechamentoCaixadto{" + "valorTotalEmVendasPorUsuario=" + valorTotalEmVendasPorUsuario + ", valorInicialTroco=" + valorInicialTroco + ", valorTotalSangria=" + valorTotalSangria + '}';
  }
  
}
