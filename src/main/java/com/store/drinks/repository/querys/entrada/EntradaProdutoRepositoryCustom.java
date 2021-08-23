
package com.store.drinks.repository.querys.entrada;

import com.store.drinks.entidade.EntradaProduto;
import com.store.drinks.entidade.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EntradaProdutoRepositoryCustom {
    public Page<EntradaProduto> filtrar(EntradasFilter filtro, Pageable pageable);
    public Page<Produto> filtrarProdutosSelect(String descricao, Pageable pageable);
}
