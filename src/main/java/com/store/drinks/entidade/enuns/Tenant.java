package com.store.drinks.entidade.enuns;

public enum Tenant {

  nome("tenant");

  private final String value;

  private Tenant(String value) {
    this.value = value;
  }

  public String value() {
    return value;
  }

}
