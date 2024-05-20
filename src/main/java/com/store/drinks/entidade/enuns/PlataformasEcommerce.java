
package com.store.drinks.entidade.enuns;

public enum PlataformasEcommerce {
  
  MERCADO_LIVRE("Mercado Livre"),
  MAGALU("Magalu"),
  SHOPEE("Shopee");
  
  private final String descricao;
  
  private PlataformasEcommerce(String descricao) {
    this.descricao = descricao;
  }
  
  public String descricao() {
    return descricao;
  }
  
}
