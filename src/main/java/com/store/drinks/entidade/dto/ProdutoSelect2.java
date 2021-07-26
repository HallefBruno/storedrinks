
package com.store.drinks.entidade.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProdutoSelect2 {
    
    private String id;
    private String text;
    private String descricaoProduto;
    private String codBarra;
    private String codProduto;
    private String quantidade;
    private String valorCusto;
    private String valorVenda;
    
    public ProdutoSelect2() {
    }

    public ProdutoSelect2(String id, String text, String descricaoProduto, String codBarra, String codProduto, String quantidade, String valorCusto, String valorVenda) {
        this.id = id;
        this.text = text;
        this.descricaoProduto = descricaoProduto;
        this.codBarra = codBarra;
        this.codProduto = codProduto;
        this.quantidade = quantidade;
        this.valorCusto = valorCusto;
        this.valorVenda = valorVenda;
    }

    
}
