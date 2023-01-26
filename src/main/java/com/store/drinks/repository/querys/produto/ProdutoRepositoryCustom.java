package com.store.drinks.repository.querys.produto;

import com.store.drinks.repository.filtros.ProdutoFiltro;
import com.store.drinks.entidade.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProdutoRepositoryCustom {

  public Page<Produto> filtrar(ProdutoFiltro filtro, Pageable pageable);
  public Page<Produto> filtrarProdutosSelect(String descricao, Pageable pageable);
  public void verificarExistenciaProduto(Produto produto);
}
