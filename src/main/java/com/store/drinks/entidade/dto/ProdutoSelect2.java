
package com.store.drinks.entidade.dto;

public class ProdutoSelect2 {
    
    private String id;
    private String text;

    public ProdutoSelect2() {
    }

    public ProdutoSelect2(String id, String text, String codProduto) {
        this.id = id;
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
