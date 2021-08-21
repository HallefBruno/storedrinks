
package com.store.drinks.repository.querys.fornecedor;

import com.store.drinks.entidade.Fornecedor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface FornecedorRepositoryCustom {
    public void verificarExistenciaFornecedor(Fornecedor fornecedor);
    public Page<Fornecedor> filtrar(FornecedorFilter fornecedorFilter, Pageable pageable);
}
