
package com.store.drinks.repository.querys.produto;

import com.store.drinks.entidade.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProdutoRepositoryCustom {
    public Page<Produto> filtrar(ProdutoFilter filtro, Pageable pageable);
}
