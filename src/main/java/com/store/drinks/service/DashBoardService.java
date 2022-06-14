package com.store.drinks.service;

import com.store.drinks.entidade.dto.produtosMaisVendidos.ProdutosMaisVendidosdto;
import com.store.drinks.repository.querys.dashBoard.ProdutoMaisVendidosImpl;
import com.store.drinks.repository.querys.dashBoard.ProdutosMaisVendidosFilters;
import com.store.drinks.repository.util.ObjectMapperUtil;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashBoardService {

  private final ProdutoMaisVendidosImpl produtoMaisVendidosImpl;
  private final ObjectMapperUtil<ProdutosMaisVendidosFilters> objectMapperUtil;
  
  public List<ProdutosMaisVendidosdto> produtosMaisVendidos(String filters) {
    ProdutosMaisVendidosFilters maisVendidosFilters = objectMapperUtil.converterStringInEntity(ProdutosMaisVendidosFilters.class, filters);
    return produtoMaisVendidosImpl.listProdutosMaisVendidos(maisVendidosFilters);
  }
  
}
