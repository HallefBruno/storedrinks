

package com.store.drinks.service;

import com.store.drinks.entidade.ItensVenda;
import com.store.drinks.entidade.MovimentacaoCaixa;
import com.store.drinks.entidade.Venda;
import com.store.drinks.repository.util.Multitenancy;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VendaService {
  
  private final Multitenancy multitenancy;
  private final UsuarioService usuarioService;
  private final AbrirCaixaService abrirCaixaService;
  
  public void salvar(List<ItensVenda> itensVendas) {
    
    final String tenant = multitenancy.getTenantValue();
    
    Venda venda = new Venda();
    venda.setDataHoraVenda(LocalDateTime.now());
    venda.setTenant(tenant);
    venda.setValorTotalVenda(new BigDecimal("40.00"));
    venda.setUsurio(usuarioService.usuarioLogado());
    venda.setItensVendas(itensVendas);
    
    MovimentacaoCaixa movimentacaoCaixa = new MovimentacaoCaixa();
    movimentacaoCaixa.setAbrirCaixa(abrirCaixaService.caixaAberto().get());
    movimentacaoCaixa.setTenant(tenant);
    movimentacaoCaixa.setValorRecebido(new BigDecimal("40.00"));
    movimentacaoCaixa.setValorTroco(BigDecimal.ZERO);
    movimentacaoCaixa.setVenda(venda);
    //movimentacaoCaixa.setFormaPagamento(formaPagamentos(movimentacaoCaixa));
  }
  
}
