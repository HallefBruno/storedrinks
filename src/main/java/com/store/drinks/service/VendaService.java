
package com.store.drinks.service;

import com.store.drinks.entidade.AbrirCaixa;
import com.store.drinks.entidade.FormaPagamento;
import com.store.drinks.entidade.ItensVenda;
import com.store.drinks.entidade.MovimentacaoCaixa;
import com.store.drinks.entidade.Produto;
import com.store.drinks.entidade.Venda;
import com.store.drinks.entidade.dto.venda.ItensVendadto;
import com.store.drinks.entidade.dto.venda.Vendadto;
import com.store.drinks.repository.MovimentacaoCaixaRepository;
import com.store.drinks.repository.ProdutoRepository;
import com.store.drinks.repository.util.Multitenancy;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class VendaService {
  
  private final Multitenancy multitenancy;
  private final UsuarioService usuarioService;
  private final AbrirCaixaService abrirCaixaService;
  private final ProdutoRepository produtoRepository;
  private final MovimentacaoCaixaRepository movimentacaoCaixaRepository;
  
  @Transactional
  public void salvar(Vendadto vendadto) {
    
    final String tenant = multitenancy.getTenantValue();
    List<ItensVenda> itensVendas = new ArrayList<>();
    Venda venda = new Venda();
    MovimentacaoCaixa movimentacaoCaixa = new MovimentacaoCaixa();
    
    vendadto.getItensVenda().forEach(item -> {
      produtoRepository.findByProdutoForVenda(item.getCodigoBarra(), tenant)
      .ifPresent(produto -> {
        if(item.getQuantidade() > produto.getQuantidade()) {
          throw new ResponseStatusException(HttpStatus.NOT_FOUND, produto.getDescricaoProduto().concat(" está com estoque zerado, remover da lista"));
        } else {
          setItensVenda(produto, item, venda, itensVendas);
        }
      });
    });
    
    Set<FormaPagamento> formasPagamento = setFormaPagamento(vendadto, movimentacaoCaixa);
    setVenda(venda, itensVendas);
    setMovimentacaoCaixa(movimentacaoCaixa, venda, formasPagamento);
    movimentacaoCaixaRepository.save(movimentacaoCaixa);
  }

  private void setItensVenda(Produto produto, ItensVendadto item, Venda venda, List<ItensVenda> itensVendas) {
    int quantidadeAtual = produto.getQuantidade();
    quantidadeAtual -= item.getQuantidade();
    produto.setQuantidade(quantidadeAtual);
    ItensVenda itensVenda = new ItensVenda();
    itensVenda.setProduto(produto);
    itensVenda.setQuantidade(item.getQuantidade());
    itensVenda.setVenda(venda);
    itensVenda.setTenant(multitenancy.getTenantValue());
    itensVendas.add(itensVenda);
  }
  
  private Set<FormaPagamento> setFormaPagamento(Vendadto vendadto, MovimentacaoCaixa movimentacaoCaixa) {
    ModelMapper modelMapper = new ModelMapper();
    Set<FormaPagamento> formasPagamento = modelMapper.map(vendadto.getFormasPagamento(), new TypeToken<Set<FormaPagamento>>(){}.getType());
    formasPagamento.forEach(formaPg -> {
      formaPg.setMovimentacaoCaixa(movimentacaoCaixa);
    });
    return formasPagamento;
  }
  
  private void setVenda(Venda venda, List<ItensVenda> itensVendas) {
    venda.setDataHoraVenda(LocalDateTime.now());
    venda.setItensVendas(itensVendas);
    venda.setUsurio(usuarioService.usuarioLogado());
    venda.setTenant(multitenancy.getTenantValue());
    venda.setValorTotalVenda(valorTotalVenda(itensVendas).setScale(2, RoundingMode.HALF_UP));
  }
  
  private void setMovimentacaoCaixa(MovimentacaoCaixa movimentacaoCaixa, Venda venda, Set<FormaPagamento> formasPagamento) {
    AbrirCaixa abrirCaixa = new AbrirCaixa();
    abrirCaixa.setId(1L);
    abrirCaixa.setUsuario(usuarioService.usuarioLogado());
    abrirCaixa.setTenant(multitenancy.getTenantValue());
    movimentacaoCaixa.setFormaPagamentos(formasPagamento);
    movimentacaoCaixa.setAbrirCaixa(abrirCaixa);
    movimentacaoCaixa.setVenda(venda);
    movimentacaoCaixa.setValorTroco(somaValorFormaPagamento(formasPagamento).subtract(venda.getValorTotalVenda()));
    movimentacaoCaixa.setValorRecebido(somaValorFormaPagamento(formasPagamento));
  }
  
  private BigDecimal valorTotalVenda(List<ItensVenda> itensVenda) {
    BigDecimal valorTotalVenda = BigDecimal.ZERO;
    for(ItensVenda itens : itensVenda) {
      valorTotalVenda = valorTotalVenda.add(itens.getProduto().getValorVenda());
    }
    return valorTotalVenda;
  }
  
  private BigDecimal somaValorFormaPagamento(Set<FormaPagamento> formasPagamento) {
    BigDecimal somaValorFormaPagamento = formasPagamento.stream()
      .map(FormaPagamento::getValor).reduce(BigDecimal.ZERO, BigDecimal::add);
    return somaValorFormaPagamento.setScale(2, RoundingMode.HALF_UP);
  }
  
}
