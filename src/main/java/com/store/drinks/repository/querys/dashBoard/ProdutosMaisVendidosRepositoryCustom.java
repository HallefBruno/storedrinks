package com.store.drinks.repository.querys.dashBoard;

import com.store.drinks.entidade.dto.produtosMaisVendidos.ProdutosMaisVendidosdto;
import java.util.List;

public interface ProdutosMaisVendidosRepositoryCustom {
  List<ProdutosMaisVendidosdto> listProdutosMaisVendidos(ProdutosMaisVendidosFilters filters);
}
