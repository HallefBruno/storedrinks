package com.store.drinks.controller;

import com.store.drinks.entidade.FormaPagamento;
import com.store.drinks.entidade.dto.usuario.UsuarioMovimentacaoCaixadto;
import com.store.drinks.repository.FormaPagamentoRepository;
import com.store.drinks.service.MovimentacaoCaixaService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/movimentacao-caixa")
@RequiredArgsConstructor
public class MovimentacaoCaixaController {
  
  private final MovimentacaoCaixaService movimentacaoCaixaService;
  private final FormaPagamentoRepository formaPagamentoRepository;
  
  @GetMapping
  public ModelAndView pageIndex() {
    return new ModelAndView("movimentacaocaixa/MovimentacaoCaixa");
  }
  
  @GetMapping("/movimentacoes")
  public ResponseEntity<?> movimentacoes(
    @RequestParam(name = "draw", required = false) Integer draw,
    @RequestParam(name = "start", required = false) Integer start,
    @RequestParam(name = "length", required = false) Integer length,
    @RequestParam(name = "isCaixaFechado", required = true) Boolean isCaixaFechado,
    @RequestParam(name = "movimentacoesCaixaFilters", required = false) String movimentacoesCaixaFilters)
  {
    System.out.println(draw);
    System.out.println(start);
    System.out.println(length);
    System.out.println(isCaixaFechado);
    System.out.println(movimentacoesCaixaFilters);
    var data = movimentacaoCaixaService.movimentacoesCaixa(movimentacoesCaixaFilters,draw,start);
    return ResponseEntity.ok(data);
  }
  
  @GetMapping("/usuarios")
  @PreAuthorize("hasRole('FILTRAR_POR_USUARIO_MOVIMENTACAO_CAIXA')")
  public ResponseEntity<List<UsuarioMovimentacaoCaixadto>> getUsuarios() {
    var list = movimentacaoCaixaService.getUsuarios();
    return ResponseEntity.ok(list);
  }

  @GetMapping("/formas-pagamento/{movimentacaoId}")
  public ResponseEntity<List<FormaPagamento>> formasPagamentoPorIdMovimentacao(@PathVariable(required = true, name = "movimentacaoId") Long movimentacaoId) {
    var formasPagamento = formaPagamentoRepository.findAllByMovimentacaoCaixaId(movimentacaoId);
    return ResponseEntity.ok(formasPagamento);
  }

}
