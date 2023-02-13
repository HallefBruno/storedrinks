package com.store.drinks.service;

import com.store.drinks.entidade.MovimentacaoCaixa;
import com.store.drinks.entidade.dto.movimentacaoCaixa.MovimentacaoCaixadto;
import com.store.drinks.entidade.dto.usuario.UsuarioMovimentacaoCaixadto;
import com.store.drinks.entidade.wrapper.DataTableWrapper;
import com.store.drinks.repository.MovimentacaoCaixaRepository;
import com.store.drinks.repository.filtros.MovimentacoesCaixaFiltro;
import com.store.drinks.repository.util.ObjectMapperUtil;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class MovimentacaoCaixaService {
  
  private final MovimentacaoCaixaRepository movimentacaoCaixaRepository;
  private final CaixaService caixaService;
  private final ObjectMapperUtil<MovimentacoesCaixaFiltro> objectMapperUtil;
  private final UsuarioService usuarioService;
  
  @Transactional
  public void salvar(BigDecimal valorRetirada) {
    if(Objects.nonNull(valorRetirada) && valorRetirada.signum() > 0) {
      var caixa = caixaService.getCaixa(null);
      var valorTotalEmCaixa = movimentacaoCaixaRepository.valorTotalEmVendasPorUsuario(caixa.getId())
        .get()
        .add(caixa.getValorInicialTroco());
      if(valorRetirada.compareTo(valorTotalEmCaixa) <= 0) {
        var movimentacao = MovimentacaoCaixa.builder()
          .recolhimento(Boolean.TRUE)
          .dataMovimentacao(LocalDateTime.now())
          .valorRecebido(BigDecimal.ZERO)
          .valorTroco(valorRetirada)
          .usuario(usuarioService.usuarioLogado())
          .caixa(caixa)
          .build();
        movimentacaoCaixaRepository.save(movimentacao);
        return;
      }
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Saldo em caixa insuficiente!");
    }
    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O valor da retirada precisa ser válido!");
  }
  
  public List<UsuarioMovimentacaoCaixadto> getUsuarios() {
    return movimentacaoCaixaRepository.usuariosMovimentacaoCaixa();
  }
  
  public DataTableWrapper<MovimentacaoCaixadto> movimentacoesCaixa(String movimentacoesCaixaFiltersString, int draw, int start) {
    MovimentacoesCaixaFiltro movimentacoesCaixaFilters = objectMapperUtil.converterStringInEntity(MovimentacoesCaixaFiltro.class, movimentacoesCaixaFiltersString);
    return movimentacaoCaixaRepository.movimentacoesCaixa(movimentacoesCaixaFilters, draw, start);
  }
}
