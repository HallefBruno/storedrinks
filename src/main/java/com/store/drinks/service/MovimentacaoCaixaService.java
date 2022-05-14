

package com.store.drinks.service;

import com.store.drinks.entidade.MovimentacaoCaixa;
import com.store.drinks.entidade.wrapper.DataTableWrapper;
import com.store.drinks.repository.MovimentacaoCaixaRepository;
import com.store.drinks.repository.querys.movimentacaoCaixa.MovimentacoesCaixaFilters;
import java.math.BigDecimal;
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
  private final UsuarioService usuarioService;
  private final CaixaService caixaService;
  
  @Transactional
  public void salvar(BigDecimal valorRetirada) {
    if(Objects.nonNull(valorRetirada) && valorRetirada.signum() > 0) {
      var caixa = caixaService.getCaixa();
      var valorTotalEmCaixa = movimentacaoCaixaRepository.valorTotalEmVendasPorUsuario()
        .get()
        .add(caixa.getValorInicialTroco());
      if(valorRetirada.compareTo(valorTotalEmCaixa) <= 0) {
        var movimentacao = MovimentacaoCaixa.builder()
          .recolhimento(Boolean.TRUE)
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
  
  public DataTableWrapper<MovimentacaoCaixa> movimentacoesCaixa(MovimentacoesCaixaFilters movimentacoesCaixaFilters, int draw, int start) {
    return movimentacaoCaixaRepository.movimentacoesCaixa(movimentacoesCaixaFilters, draw, start);
  }
}
