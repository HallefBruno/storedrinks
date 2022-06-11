package com.store.drinks.repository.querys.dashBoard;

import com.store.drinks.entidade.dto.produtosMaisVendidos.ProdutosMaisVendidosdto;
import java.time.LocalDate;
import java.util.List;

public interface ProdutosMaisVendidosRepositoryCustom {
  List<ProdutosMaisVendidosdto> listProdutosMaisVendidos(LocalDate dataInicial, LocalDate dataFinal);
}
