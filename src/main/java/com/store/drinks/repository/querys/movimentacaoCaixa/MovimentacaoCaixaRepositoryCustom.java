package com.store.drinks.repository.querys.movimentacaoCaixa;

import com.store.drinks.repository.filtros.MovimentacoesCaixaFiltro;
import com.store.drinks.entidade.dto.movimentacaoCaixa.MovimentacaoCaixadto;
import com.store.drinks.entidade.dto.usuario.UsuarioMovimentacaoCaixadto;
import com.store.drinks.entidade.wrapper.DataTableWrapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.cache.annotation.Cacheable;

public interface MovimentacaoCaixaRepositoryCustom {
  
  Optional<BigDecimal> valorTotalEmVendasPorUsuario(Long caixaId);
  DataTableWrapper<MovimentacaoCaixadto> movimentacoesCaixa(MovimentacoesCaixaFiltro movimentacoesCaixaFilters, int draw, int start);
  
  @Cacheable("usuariosMovimentacaoCaixa")
  List<UsuarioMovimentacaoCaixadto> usuariosMovimentacaoCaixa();
  
}
