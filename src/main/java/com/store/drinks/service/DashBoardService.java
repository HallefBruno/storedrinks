package com.store.drinks.service;

import com.store.drinks.entidade.dto.produtosMaisVendidos.ProdutosMaisVendidosdto;
import com.store.drinks.repository.querys.dashBoard.ProdutoMaisVendidosImpl;
import com.store.drinks.repository.filtros.ProdutosMaisVendidosFiltro;
import com.store.drinks.repository.util.ObjectMapperUtil;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashBoardService {

  private final ProdutoMaisVendidosImpl produtoMaisVendidosImpl;
  private final ObjectMapperUtil<ProdutosMaisVendidosFiltro> objectMapperUtil;
  
  public List<ProdutosMaisVendidosdto> produtosMaisVendidos(String filters) {
    ProdutosMaisVendidosFiltro maisVendidosFilters = objectMapperUtil.converterStringInEntity(ProdutosMaisVendidosFiltro.class, filters);
    return produtoMaisVendidosImpl.listProdutosMaisVendidos(maisVendidosFilters);
  }
  
}
