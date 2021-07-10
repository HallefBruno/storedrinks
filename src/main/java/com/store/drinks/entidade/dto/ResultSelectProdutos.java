
package com.store.drinks.entidade.dto;

import java.util.List;

public class ResultSelectProdutos {
    
    private Long totalItens;
    private List<ProdutoSelect2> items;

    public Long getTotalItens() {
        return totalItens;
    }

    public void setTotalItens(Long totalItens) {
        this.totalItens = totalItens;
    }

    public List<ProdutoSelect2> getItems() {
        return items;
    }

    public void setItems(List<ProdutoSelect2> items) {
        this.items = items;
    }
    
    
    
}
