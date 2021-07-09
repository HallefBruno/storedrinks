
package com.store.drinks.entidade;

public enum SituacaoCompra {
    
    CONFIRMADA("Confirmada"),
    ABERTA("Aberta");
    
    private String value;

    private SituacaoCompra(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}