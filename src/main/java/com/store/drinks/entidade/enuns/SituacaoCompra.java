package com.store.drinks.entidade.enuns;

public enum SituacaoCompra {

  CONFIRMADA("Confirmada"),
  ABERTA("Aberta");

  private final String value;

  private SituacaoCompra(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
