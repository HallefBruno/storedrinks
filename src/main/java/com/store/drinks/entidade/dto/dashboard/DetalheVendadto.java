package com.store.drinks.entidade.dto.dashboard;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetalheVendadto implements Serializable {

  private Long idVenda;
  private String nomeProduto;
  private Integer quantidade;
  private BigDecimal valorTotal;
  private String nomeVendedor;
  private LocalDateTime dataHoraVenda;

  public DetalheVendadto() {
  }

  public DetalheVendadto(Long idVenda, String nomeProduto, Integer quantidade, BigDecimal valorTotal, String nomeVendedor, LocalDateTime dataHoraVenda) {
    this.idVenda = idVenda;
    this.nomeProduto = nomeProduto;
    this.quantidade = quantidade;
    this.valorTotal = valorTotal;
    this.nomeVendedor = nomeVendedor;
    this.dataHoraVenda = dataHoraVenda;
  }
  
  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + Objects.hashCode(this.idVenda);
    hash = 67 * hash + Objects.hashCode(this.nomeProduto);
    hash = 67 * hash + Objects.hashCode(this.quantidade);
    hash = 67 * hash + Objects.hashCode(this.valorTotal);
    hash = 67 * hash + Objects.hashCode(this.nomeVendedor);
    hash = 67 * hash + Objects.hashCode(this.dataHoraVenda);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final DetalheVendadto other = (DetalheVendadto) obj;
    if (!Objects.equals(this.nomeProduto, other.nomeProduto)) {
      return false;
    }
    if (!Objects.equals(this.nomeVendedor, other.nomeVendedor)) {
      return false;
    }
    if (!Objects.equals(this.idVenda, other.idVenda)) {
      return false;
    }
    if (!Objects.equals(this.quantidade, other.quantidade)) {
      return false;
    }
    if (!Objects.equals(this.valorTotal, other.valorTotal)) {
      return false;
    }
    return Objects.equals(this.dataHoraVenda, other.dataHoraVenda);
  }

  @Override
  public String toString() {
    return "DetalheVendadto{" + "idVenda=" + idVenda + ", nomeProduto=" + nomeProduto + ", quantidade=" + quantidade + ", valorTotal=" + valorTotal + ", nomeVendedor=" + nomeVendedor + ", dataHoraVenda=" + dataHoraVenda + '}';
  }
  
}
