package com.store.drinks.entidade.dto.caixa;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public class DetalheSangriadto implements Serializable {
  
  private BigDecimal valor;
  private String nomeUsuario;
  private LocalDateTime dataSangria;

  public DetalheSangriadto() {
  }

  public DetalheSangriadto(BigDecimal valor, String nomeUsuario, LocalDateTime dataSangria) {
    this.valor = valor;
    this.nomeUsuario = nomeUsuario;
    this.dataSangria = dataSangria;
  }

  public BigDecimal getValor() {
    return valor;
  }

  public void setValor(BigDecimal valor) {
    this.valor = valor;
  }

  public String getNomeUsuario() {
    return nomeUsuario;
  }

  public void setNomeUsuario(String nomeUsuario) {
    this.nomeUsuario = nomeUsuario;
  }

  public LocalDateTime getDataSangria() {
    return dataSangria;
  }

  public void setDataSangria(LocalDateTime dataSangria) {
    this.dataSangria = dataSangria;
  }

  @Override
  public String toString() {
    return "DetalheSangria{" + "valor=" + valor + ", nomeUsuario=" + nomeUsuario + ", dataSangria=" + dataSangria + '}';
  }
  
}
