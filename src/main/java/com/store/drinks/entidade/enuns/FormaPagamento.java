package com.store.drinks.entidade.enuns;

public enum FormaPagamento {

  PIX("Pix"),
  CARTAO_CREDITO("Cartão crédito"),
  CARTAO_DEBITO("Cartão débito"),
  DINHEIRO("Dinheiro");

  private final String value;

  private FormaPagamento(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
