

package com.store.drinks.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovimentacaoCaixaService {
  
//  private final MovimentacaoCaixaRepository movimentacaoCaixaRepository;
//  private final AbrirCaixaRepository abrirCaixaRepository;
//  private final UsuarioService usuarioService;
//  private final ProdutoRepository produtoRepository;
//  
//  @Transactional
//  public void salvar() {
//    movimentacaoCaixaRepository.save(movimentacaoCaixa());
//  }
//  
//  public MovimentacaoCaixa movimentacaoCaixa() {
//    final String tenant = "sud";
//    Usuario usuario = usuarioService.findByEmailAndAtivoTrue("sud@storedrink.com").get();
//    
//    Venda venda = new Venda();
//    AbrirCaixa abrirCaixa = new AbrirCaixa();
//    
//    abrirCaixa.setAberto(Boolean.TRUE);
//    abrirCaixa.setDataHoraAbertura(LocalDateTime.now());
//    abrirCaixa.setTenant(tenant);
//    abrirCaixa.setValorInicialTroco(BigDecimal.TEN);
//    abrirCaixa.setUsuario(usuario);
//    
//    abrirCaixaRepository.save(abrirCaixa);
//    
//    venda.setDataHoraVenda(LocalDateTime.now());
//    venda.setTenant(tenant);
//    venda.setItensVendas(getItensVendas(venda));
//    venda.setValorTotalVenda(new BigDecimal("40.00"));
//    venda.setUsurio(usuario);
//    
//    MovimentacaoCaixa movimentacaoCaixa = new MovimentacaoCaixa();
//    movimentacaoCaixa.setAbrirCaixa(abrirCaixa);
//    movimentacaoCaixa.setTenant(tenant);
//    movimentacaoCaixa.setValorRecebido(new BigDecimal("40.00"));
//    movimentacaoCaixa.setValorTroco(BigDecimal.ZERO);
//    movimentacaoCaixa.setVenda(venda);
//    movimentacaoCaixa.setFormaPagamentos(formaPagamentos(movimentacaoCaixa));
//    return movimentacaoCaixa;
//    
//  }
//  
//  private Produto produto() {
//    Produto produto = produtoRepository.findById(1L).get();
//    return produto;
//  }
//  
//  private ItensVenda getItensVenda(Venda venda) {
//    final String tenant = "sud";
//    ItensVenda itensVenda = new ItensVenda();
//    itensVenda.setProduto(produto());
//    itensVenda.setQuantidade(1);
//    itensVenda.setTenant(tenant);
//    itensVenda.setVenda(venda);
//    return itensVenda;
//  }
//  
//  private List<ItensVenda> getItensVendas(Venda venda) {
//    return List.of(getItensVenda(venda));
//  }
//  
//  private Set<FormaPagamento> formaPagamentos(MovimentacaoCaixa movimentacaoCaixa) {
//    FormaPagamento formaPagamento1 = new FormaPagamento();
//    formaPagamento1.setMovimentacaoCaixa(movimentacaoCaixa);
//    formaPagamento1.setTipoPagamento(TipoPagamento.CARTAO_DEBITO);
//    formaPagamento1.setValor(new BigDecimal("20.00"));
//    
//    FormaPagamento formaPagamento2 = new FormaPagamento();
//    formaPagamento2.setMovimentacaoCaixa(movimentacaoCaixa);
//    formaPagamento2.setTipoPagamento(TipoPagamento.DINHEIRO);
//    formaPagamento2.setValor(new BigDecimal("20.00"));
//    
//    return Set.of(formaPagamento1, formaPagamento2);
//  }
  
}
