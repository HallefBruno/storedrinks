
package com.store.drinks.service;

import com.store.drinks.entidade.FormaPagamento;
import com.store.drinks.entidade.ItensVenda;
import com.store.drinks.entidade.MovimentacaoCaixa;
import com.store.drinks.entidade.Produto;
import com.store.drinks.entidade.Venda;
import com.store.drinks.entidade.dto.venda.CancelarVendadto;
import com.store.drinks.entidade.dto.venda.ItensVendaCancelardto;
import com.store.drinks.entidade.dto.venda.ItensVendadto;
import com.store.drinks.entidade.dto.venda.Vendadto;
import com.store.drinks.repository.ItensVendaRepository;
import com.store.drinks.repository.MovimentacaoCaixaRepository;
import com.store.drinks.repository.ProdutoRepository;
import com.store.drinks.repository.VendaRepository;
import com.store.drinks.repository.util.Multitenancy;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class VendaService {
  
  private final Multitenancy multitenancy;
  private final UsuarioService usuarioService;
  private final CaixaService abrirCaixaService;
  private final ProdutoRepository produtoRepository;
  private final MovimentacaoCaixaRepository movimentacaoCaixaRepository;
  private final VendaRepository vendaRepository;
  private final ItensVendaRepository itensVendaRepository;
  
  public Page<CancelarVendadto> getListVendasCancelar(Pageable pageable) {
    return vendaRepository.getVendasCancelar(pageable);
  }
  
  public List<ItensVendaCancelardto> getItensVenda(Long vendaId) {
    if(Objects.isNull(vendaId) || vendaId < 0) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Identificador inválido!");
    }
    return vendaRepository.getItensVenda(vendaId);
  }
  
  @Transactional
  public void salvar(Vendadto vendadto) {
    
    List<ItensVenda> itensVendas = new ArrayList<>();
    Venda venda = new Venda();
    MovimentacaoCaixa movimentacaoCaixa = new MovimentacaoCaixa();
    
    vendadto.getItensVenda().forEach(item -> {
      produtoRepository.findByProdutoForUpdate(item.getCodigoBarra(), tenant())
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
  
  @Transactional
  public void excluirVenda(Long movimentacaoCaixaId, Long vendaId) {
    if(Objects.isNull(movimentacaoCaixaId) || movimentacaoCaixaId < 0) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Identificador movimentação inválido!");
    }
    if(Objects.isNull(vendaId) || vendaId < 0) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Identificador venda inválido!");
    }
    
    itensVendaRepository.findAllByVendaId(vendaId).forEach(itens -> {
      produtoRepository.findByProdutoForUpdate(itens.getProduto().getCodigoBarra(), tenant())
      .ifPresent(produto -> {
        int quantidadeAutal = produto.getQuantidade();
        produto.setQuantidade(quantidadeAutal + itens.getQuantidade());
        produtoRepository.save(produto);
      });
    });
    movimentacaoCaixaRepository.deleteById(movimentacaoCaixaId);
  }

  private void setItensVenda(Produto produto, ItensVendadto item, Venda venda, List<ItensVenda> itensVendas) {
    int quantidadeAtual = produto.getQuantidade();
    quantidadeAtual -= item.getQuantidade();
    produto.setQuantidade(quantidadeAtual);
    ItensVenda itensVenda = new ItensVenda();
    itensVenda.setProduto(produto);
    itensVenda.setQuantidade(item.getQuantidade());
    itensVenda.setVenda(venda);
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
    venda.setUsuario(usuarioService.usuarioLogado());
    venda.setValorTotalVenda(valorTotalVenda(itensVendas).setScale(2, RoundingMode.HALF_UP));
  }
  
  private void setMovimentacaoCaixa(MovimentacaoCaixa movimentacaoCaixa, Venda venda, Set<FormaPagamento> formasPagamento) {
    movimentacaoCaixa.setFormaPagamentos(formasPagamento);
    movimentacaoCaixa.setCaixa(abrirCaixaService.getCaixa(null));
    movimentacaoCaixa.setVenda(venda);
    movimentacaoCaixa.setUsuario(usuarioService.usuarioLogado());
    movimentacaoCaixa.setValorTroco(somaValorFormaPagamento(formasPagamento).subtract(venda.getValorTotalVenda()));
    movimentacaoCaixa.setValorRecebido(somaValorFormaPagamento(formasPagamento));
    movimentacaoCaixa.setDataMovimentacao(venda.getDataHoraVenda());
  }
  
  private BigDecimal valorTotalVenda(List<ItensVenda> itensVenda) {
    BigDecimal valorTotalVenda = BigDecimal.ZERO;
    for(ItensVenda itens : itensVenda) {
      valorTotalVenda = valorTotalVenda.add(itens.getProduto().getValorVenda().multiply(BigDecimal.valueOf(itens.getQuantidade())));
    }
    return valorTotalVenda;
  }
  
  private BigDecimal somaValorFormaPagamento(Set<FormaPagamento> formasPagamento) {
    BigDecimal somaValorFormaPagamento = formasPagamento.stream()
      .map(FormaPagamento::getValor).reduce(BigDecimal.ZERO, BigDecimal::add);
    return somaValorFormaPagamento.setScale(2, RoundingMode.HALF_UP);
  }
  
  private String tenant() {
    return multitenancy.getTenantValue();
  }
  
}
