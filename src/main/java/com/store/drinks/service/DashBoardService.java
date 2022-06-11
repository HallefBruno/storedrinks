

package com.store.drinks.service;

import com.store.drinks.entidade.dto.produtosMaisVendidos.ProdutosMaisVendidosdto;
import com.store.drinks.repository.querys.dashBoard.ProdutoMaisVendidosImpl;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashBoardService {

  private final ProdutoMaisVendidosImpl produtoMaisVendidosImpl;
  
  public List<ProdutosMaisVendidosdto> produtosMaisVendidos() {
    return produtoMaisVendidosImpl.listProdutosMaisVendidos(LocalDate.now(), LocalDate.now());
  }
  
}
