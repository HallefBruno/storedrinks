package com.store.drinks.repository.querys.dashboard;

import com.store.drinks.entidade.dto.dashboard.DetalheProdutodto;
import com.store.drinks.entidade.dto.dashboard.DetalheVendadto;
import com.store.drinks.entidade.dto.dashboard.TotalCustodto;
import com.store.drinks.entidade.dto.dashboard.TotalVendadto;
import com.store.drinks.repository.filtros.ProdutosMaisVendidosFiltro;
import com.store.drinks.entidade.dto.produtosMaisVendidos.ProdutosMaisVendidosdto;
import com.store.drinks.entidade.wrapper.DataTableWrapper;
import java.util.List;

public interface ProdutosMaisVendidosRepositoryCustom {
  List<ProdutosMaisVendidosdto> listProdutosMaisVendidos(ProdutosMaisVendidosFiltro filters);
  DataTableWrapper<DetalheVendadto> listVendasTempoReal(Long idUsuario, Integer draw, Integer start, Integer length);
  List<DetalheProdutodto> listDetalheProdutoVendido(Long idVenda);
  TotalVendadto totalVenda(Long idUsuario);
  TotalCustodto totalCusto(Long idUsuario);
}
