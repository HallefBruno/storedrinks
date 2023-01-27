package com.store.drinks.entidade.enuns;

public enum SuperUser {
  TENANT("sud");
  private final String value;

  private SuperUser(String value) {
    this.value = value;
  }
  public String get() {
    return this.value;
  }
}
