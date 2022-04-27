
package com.store.drinks.service;

import com.store.drinks.entidade.FormaPagamento;
import com.store.drinks.entidade.ItensVenda;
import com.store.drinks.entidade.MovimentacaoCaixa;
import com.store.drinks.entidade.Produto;
import com.store.drinks.entidade.Venda;
import com.store.drinks.entidade.dto.venda.Vendadto;
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
  
  @Transactional
  public void salvar(Vendadto vendadto) {
    
    final String tenant = multitenancy.getTenantValue();
    List<ItensVenda> itensVendas = new ArrayList<>();
    Venda venda = new Venda();
    MovimentacaoCaixa movimentacaoCaixa = new MovimentacaoCaixa();
    
    vendadto.getItensVenda().forEach(item -> {
      produtoRepository.findByProdutoForVenda(item.getCodigoBarra(), multitenancy.getTenantValue())
      .ifPresent(produto -> {
        if(item.getQuantidade() > produto.getQuantidade()) {
          throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Você não possui essa quantidade em estoque!");
        } else {
          ItensVenda itensVenda = new ItensVenda();
          itensVenda.setProduto(produto);
          itensVenda.setQuantidade(item.getQuantidade());
          itensVenda.setVenda(venda);
          itensVenda.setTenant(tenant);
          itensVendas.add(itensVenda);
        }
      });
    });
    
    ModelMapper modelMapper = new ModelMapper();
    Set<FormaPagamento> formasPagamento = modelMapper.map(vendadto.getFormasPagamento(), new TypeToken<Set<FormaPagamento>>(){}.getType());
    formasPagamento.forEach(formaPg -> {
      formaPg.setMovimentacaoCaixa(movimentacaoCaixa);
    });
    
    venda.setDataHoraVenda(LocalDateTime.now());
    venda.setItensVendas(itensVendas);
    venda.setUsurio(usuarioService.usuarioLogado());
    venda.setTenant(tenant);
    venda.setValorTotalVenda(valorTotalVenda(itensVendas).setScale(2, RoundingMode.HALF_UP));
    
    movimentacaoCaixa.setFormaPagamentos(formasPagamento);
    movimentacaoCaixa.setAbrirCaixa(abrirCaixaService.caixaAberto().get());
    movimentacaoCaixa.setVenda(venda);
    movimentacaoCaixa.setValorTroco(venda.getValorTotalVenda().subtract(somaValorFormaPagamento(formasPagamento)));
    movimentacaoCaixa.setValorRecebido(venda.getValorTotalVenda());
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
    return somaValorFormaPagamento;
  }
  
}
