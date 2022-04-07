package com.store.drinks.entidade.enuns;

public enum TipoPagamento {

  PIX("Pix"),
  CARTAO_CREDITO("Cartão crédito"),
  CARTAO_DEBITO("Cartão débito"),
  DINHEIRO("Dinheiro");

  private final String value;

  private TipoPagamento(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
