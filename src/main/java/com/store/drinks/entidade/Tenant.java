
package com.store.drinks.entidade;

public enum Tenant {
    
    nome("tenant");
    
    private String value;

    private Tenant(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
    
}
