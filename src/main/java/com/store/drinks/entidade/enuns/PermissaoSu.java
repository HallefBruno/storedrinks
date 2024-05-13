package com.store.drinks.entidade.enuns;

public enum PermissaoSu {
  SUPER_USER("ROLE_SUPER_USER");
  private final String value;

  private PermissaoSu(String value) {
    this.value = value;
  }
  public String get() {
    return this.value;
  }
}
