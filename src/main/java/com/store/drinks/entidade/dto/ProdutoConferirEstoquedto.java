
package com.store.drinks.entidade.dto;

import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProdutoConferirEstoquedto implements Serializable {
  
  private Long id;
  private Boolean ativo;
  private String codProduto;
  private String codBarra;
  private String descricaoProduto;
  private Integer quantidade;

  public ProdutoConferirEstoquedto(Long id, Boolean ativo, String codProduto, String codBarra, String descricaoProduto, Integer quantidade) {
    this.id = id;
    this.ativo = ativo;
    this.codProduto = codProduto;
    this.codBarra = codBarra;
    this.descricaoProduto = descricaoProduto;
    this.quantidade = quantidade;
  }

  public ProdutoConferirEstoquedto() {
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 79 * hash + Objects.hashCode(this.id);
    hash = 79 * hash + Objects.hashCode(this.ativo);
    hash = 79 * hash + Objects.hashCode(this.codProduto);
    hash = 79 * hash + Objects.hashCode(this.codBarra);
    hash = 79 * hash + Objects.hashCode(this.descricaoProduto);
    hash = 79 * hash + Objects.hashCode(this.quantidade);
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
    final ProdutoConferirEstoquedto other = (ProdutoConferirEstoquedto) obj;
    if (!Objects.equals(this.codProduto, other.codProduto)) {
      return false;
    }
    if (!Objects.equals(this.codBarra, other.codBarra)) {
      return false;
    }
    if (!Objects.equals(this.descricaoProduto, other.descricaoProduto)) {
      return false;
    }
    if (!Objects.equals(this.id, other.id)) {
      return false;
    }
    if (!Objects.equals(this.ativo, other.ativo)) {
      return false;
    }
    return Objects.equals(this.quantidade, other.quantidade);
  }

  @Override
  public String toString() {
    return "ProdutoConferirEstoquedto{" + "id=" + id + ", ativo=" + ativo + ", codProduto=" + codProduto + ", codBarra=" + codBarra + ", descricaoProduto=" + descricaoProduto + ", quantidade=" + quantidade + '}';
  }
  
}
