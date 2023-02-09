
package com.store.drinks.entidade.dto.dashboard;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetalheProdutodto implements Serializable {
  
  private String descricaoProduto;
  private BigDecimal valorVenda;
  private Integer quantidade;
  private BigDecimal valorTotal;

  public DetalheProdutodto() {
  }

  public DetalheProdutodto(String descricaoProduto, BigDecimal valorUnitario, Integer quantidade, BigDecimal valorTotal) {
    this.descricaoProduto = descricaoProduto;
    this.valorVenda = valorUnitario;
    this.quantidade = quantidade;
    this.valorTotal = valorTotal;
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 37 * hash + Objects.hashCode(this.descricaoProduto);
    hash = 37 * hash + Objects.hashCode(this.valorVenda);
    hash = 37 * hash + Objects.hashCode(this.quantidade);
    hash = 37 * hash + Objects.hashCode(this.valorTotal);
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
    final DetalheProdutodto other = (DetalheProdutodto) obj;
    if (!Objects.equals(this.descricaoProduto, other.descricaoProduto)) {
      return false;
    }
    if (!Objects.equals(this.valorVenda, other.valorVenda)) {
      return false;
    }
    if (!Objects.equals(this.quantidade, other.quantidade)) {
      return false;
    }
    return Objects.equals(this.valorTotal, other.valorTotal);
  }
  
}
