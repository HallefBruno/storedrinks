package com.store.drinks.repository.querys.fornecedor;

import com.store.drinks.repository.filtros.FornecedorFiltro;
import com.store.drinks.entidade.Fornecedor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FornecedorRepositoryCustom {

  public void verificarExistenciaFornecedor(Fornecedor fornecedor);
  public Page<Fornecedor> filtrar(FornecedorFiltro fornecedorFilter, Pageable pageable);
}
