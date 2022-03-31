package com.store.drinks.service;

import com.store.drinks.entidade.EntradaProduto;
import com.store.drinks.entidade.Produto;
import com.store.drinks.entidade.enuns.SituacaoCompra;
import com.store.drinks.entidade.dto.ProdutoSelect2;
import com.store.drinks.entidade.dto.ResultSelectProdutos;
import com.store.drinks.execption.NegocioException;
import com.store.drinks.repository.EntradaProdutoRepository;
import com.store.drinks.repository.ProdutoRepository;
import com.store.drinks.repository.util.Multitenancy;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EntradaProdutoService {

  private final EntradaProdutoRepository entradaProdutoRepository;
  private final ProdutoRepository produtoRepository;
  private final Multitenancy multitenancy;

  @Transactional
  public void salvar(EntradaProduto entradaProduto) {
    Produto produto = buscarProdutoPorCodBarra(entradaProduto.getCodigoBarra());
    setarNovosValores(entradaProduto, produto);
    produto = produtoRepository.save(produto);
    entradaProduto.setProduto(produto);
    entradaProdutoRepository.save(entradaProduto);
  }

  @Transactional
  public void alterarSituacaoEntrada(Long id) {
    if (Objects.nonNull(id)) {
      Optional<EntradaProduto> optionalEntrada = entradaProdutoRepository.findById(id);
      if (optionalEntrada.isPresent()) {
        EntradaProduto entradaProduto = optionalEntrada.get();
        Produto produto = entradaProduto.getProduto();
        produto.setQuantidade(entradaProduto.getQuantidadeIncrementar() + produto.getQuantidade());
        entradaProduto.setNovaQuantidade(produto.getQuantidade());
        entradaProduto.setProduto(produto);
        entradaProduto.setSituacaoCompra(SituacaoCompra.CONFIRMADA);
        entradaProduto.setValorTotal(totalEntrada(entradaProduto.getQuantidadeIncrementar(), entradaProduto.getNovoValorCusto()));
        entradaProdutoRepository.save(entradaProduto);
        return;
      }
      throw new NegocioException("Nenhuma entrada encontrada!");
    }
    throw new NegocioException("Identificador inválido!");
  }

  private void setarNovosValores(EntradaProduto entradaProduto, Produto produto) {
    if (((entradaProduto.getNovaQuantidade() == null || entradaProduto.getNovaQuantidade() == 0) && (entradaProduto.getNovoValorCusto() == null || entradaProduto.getNovoValorCusto().doubleValue() == 0) && (entradaProduto.getNovoValorVenda() == null || entradaProduto.getNovoValorVenda().doubleValue() == 0))) {
      if (entradaProduto.getSituacaoCompra() == SituacaoCompra.CONFIRMADA) {
        entradaProduto.setNovaQuantidade(produto.getQuantidade() + produto.getQuantidade());
      } else {
        entradaProduto.setNovaQuantidade(produto.getQuantidade());
        entradaProduto.setQuantidadeIncrementar(produto.getQuantidade());
      }
      entradaProduto.setNovoValorCusto(produto.getValorCusto());
      entradaProduto.setNovoValorVenda(produto.getValorVenda());
      BigDecimal somaTotal = BigDecimal.valueOf(produto.getQuantidade() * produto.getValorCusto().doubleValue());
      entradaProduto.setValorTotal(somaTotal);
      produto.setQuantidade(entradaProduto.getNovaQuantidade());
    } else if (((entradaProduto.getNovaQuantidade() != null && entradaProduto.getNovaQuantidade() > 0) && (entradaProduto.getNovoValorCusto() == null || entradaProduto.getNovoValorCusto().doubleValue() == 0) && (entradaProduto.getNovoValorVenda() == null || entradaProduto.getNovoValorVenda().doubleValue() == 0))) {
      if (entradaProduto.getSituacaoCompra() == SituacaoCompra.CONFIRMADA) {
        produto.setQuantidade(produto.getQuantidade() + entradaProduto.getNovaQuantidade());
      } else {
        produto.setQuantidade(produto.getQuantidade());
        entradaProduto.setQuantidadeIncrementar(entradaProduto.getNovaQuantidade());
      }
      entradaProduto.setNovoValorCusto(produto.getValorCusto());
      entradaProduto.setNovoValorVenda(produto.getValorVenda());
      BigDecimal somaTotal = BigDecimal.valueOf(entradaProduto.getNovaQuantidade() * produto.getValorCusto().doubleValue());
      entradaProduto.setValorTotal(somaTotal);
      entradaProduto.setNovaQuantidade(produto.getQuantidade());
    } else if (((entradaProduto.getNovoValorCusto() != null && entradaProduto.getNovoValorCusto().doubleValue() > 0) && (entradaProduto.getNovoValorVenda() == null || entradaProduto.getNovoValorVenda().doubleValue() == 0) && (entradaProduto.getNovaQuantidade() == null || entradaProduto.getNovaQuantidade() == 0))) {
      if (entradaProduto.getSituacaoCompra() == SituacaoCompra.CONFIRMADA) {
        entradaProduto.setNovaQuantidade(produto.getQuantidade() + produto.getQuantidade());
      } else {
        entradaProduto.setNovaQuantidade(produto.getQuantidade());
        entradaProduto.setQuantidadeIncrementar(produto.getQuantidade());
      }
      produto.setValorCusto(entradaProduto.getNovoValorCusto());
      entradaProduto.setNovoValorVenda(produto.getValorVenda());
      BigDecimal somaTotal = BigDecimal.valueOf(produto.getQuantidade() * entradaProduto.getNovoValorCusto().doubleValue());
      entradaProduto.setValorTotal(somaTotal);
      produto.setQuantidade(entradaProduto.getNovaQuantidade());
    } else if (((entradaProduto.getNovoValorVenda() != null && entradaProduto.getNovoValorVenda().doubleValue() > 0) && (entradaProduto.getNovoValorCusto() == null || entradaProduto.getNovoValorCusto().doubleValue() == 0) && (entradaProduto.getNovaQuantidade() == null || entradaProduto.getNovaQuantidade() == 0))) {
      if (entradaProduto.getSituacaoCompra() == SituacaoCompra.CONFIRMADA) {
        entradaProduto.setNovaQuantidade(produto.getQuantidade() + produto.getQuantidade());
      } else {
        entradaProduto.setNovaQuantidade(produto.getQuantidade());
        entradaProduto.setQuantidadeIncrementar(produto.getQuantidade());
      }
      produto.setValorVenda(entradaProduto.getNovoValorVenda());
      entradaProduto.setNovoValorCusto(produto.getValorCusto());
      BigDecimal somaTotal = BigDecimal.valueOf(produto.getQuantidade() * produto.getValorCusto().doubleValue());
      entradaProduto.setValorTotal(somaTotal);
      produto.setQuantidade(entradaProduto.getNovaQuantidade());
    } else if (((entradaProduto.getNovaQuantidade() != null && entradaProduto.getNovaQuantidade() > 0) && (entradaProduto.getNovoValorCusto() != null && entradaProduto.getNovoValorCusto().doubleValue() > 0) && (entradaProduto.getNovoValorVenda() == null || entradaProduto.getNovoValorVenda().doubleValue() == 0))) {
      if (entradaProduto.getSituacaoCompra() == SituacaoCompra.CONFIRMADA) {
        produto.setQuantidade(produto.getQuantidade() + entradaProduto.getNovaQuantidade());
      } else {
        produto.setQuantidade(produto.getQuantidade());
        entradaProduto.setQuantidadeIncrementar(entradaProduto.getNovaQuantidade());
      }
      produto.setValorCusto(entradaProduto.getNovoValorCusto());
      entradaProduto.setNovoValorVenda(produto.getValorVenda());
      BigDecimal somaTotal = BigDecimal.valueOf(entradaProduto.getNovaQuantidade() * entradaProduto.getNovoValorCusto().doubleValue());
      entradaProduto.setValorTotal(somaTotal);
      entradaProduto.setNovaQuantidade(produto.getQuantidade());
    } else if (((entradaProduto.getNovaQuantidade() != null && entradaProduto.getNovaQuantidade() > 0) && (entradaProduto.getNovoValorVenda() != null && entradaProduto.getNovoValorVenda().doubleValue() > 0) && (entradaProduto.getNovoValorCusto() == null || entradaProduto.getNovoValorCusto().doubleValue() == 0))) {
      if (entradaProduto.getSituacaoCompra() == SituacaoCompra.CONFIRMADA) {
        produto.setQuantidade(produto.getQuantidade() + entradaProduto.getNovaQuantidade());
      } else {
        produto.setQuantidade(produto.getQuantidade());
        entradaProduto.setQuantidadeIncrementar(entradaProduto.getNovaQuantidade());
      }
      entradaProduto.setNovoValorCusto(produto.getValorCusto());
      produto.setValorVenda(entradaProduto.getNovoValorVenda());
      BigDecimal somaTotal = BigDecimal.valueOf(entradaProduto.getNovaQuantidade() * produto.getValorCusto().doubleValue());
      entradaProduto.setValorTotal(somaTotal);
      entradaProduto.setNovaQuantidade(produto.getQuantidade());
    } else if (((entradaProduto.getNovoValorCusto() != null && entradaProduto.getNovoValorCusto().doubleValue() > 0) && (entradaProduto.getNovoValorVenda() != null && entradaProduto.getNovoValorVenda().doubleValue() > 0) && (entradaProduto.getNovaQuantidade() == null || entradaProduto.getNovaQuantidade() == 0))) {
      if (entradaProduto.getSituacaoCompra() == SituacaoCompra.CONFIRMADA) {
        produto.setQuantidade(produto.getQuantidade() + produto.getQuantidade());
      } else {
        produto.setQuantidade(produto.getQuantidade());
        entradaProduto.setQuantidadeIncrementar(produto.getQuantidade());
      }
      produto.setValorCusto(entradaProduto.getNovoValorCusto());
      produto.setValorVenda(entradaProduto.getNovoValorVenda());
      entradaProduto.setNovaQuantidade(produto.getQuantidade());
      BigDecimal somaTotal = BigDecimal.valueOf(produto.getQuantidade() * entradaProduto.getNovoValorCusto().doubleValue());
      entradaProduto.setValorTotal(somaTotal);
    } else {
      if (entradaProduto.getSituacaoCompra() == SituacaoCompra.CONFIRMADA) {
        produto.setQuantidade(produto.getQuantidade() + entradaProduto.getNovaQuantidade());
      } else {
        produto.setQuantidade(produto.getQuantidade());
        entradaProduto.setQuantidadeIncrementar(entradaProduto.getNovaQuantidade());
      }
      produto.setValorCusto(entradaProduto.getNovoValorCusto());
      produto.setValorVenda(entradaProduto.getNovoValorVenda());
      entradaProduto.setNovaQuantidade(produto.getQuantidade());
      BigDecimal somaTotal = BigDecimal.valueOf(entradaProduto.getNovaQuantidade() * entradaProduto.getNovoValorCusto().doubleValue());
      entradaProduto.setValorTotal(somaTotal);
    }
  }

  public Produto buscarProdutoPorId(Long id) {
    Optional<Produto> opProduto = produtoRepository.findById(id);
    if (opProduto.isPresent()) {
      return opProduto.get();
    }
    throw new NegocioException("Produto não encontrado!");
  }

  public Produto buscarProdutoPorCodBarra(String codBarra) {
    if (!StringUtils.isEmpty(codBarra)) {
      Optional<Produto> produtoOptional = produtoRepository.findByCodigoBarraAndAtivoTrueAndTenant(codBarra, multitenancy.getTenantValue());
      if (produtoOptional.isPresent()) {
        return produtoOptional.get();
      }
      throw new NegocioException("Nenhum produto encontrado!");
    }
    throw new NegocioException("Código produto inválido!");
  }

  public ResultSelectProdutos pesquisarProdutosAutoComplete(String descricao, String pagina) {
    ProdutoSelect2 produtoDTO = new ProdutoSelect2();
    ResultSelectProdutos resultSelectAutomoveis = new ResultSelectProdutos();
    List<ProdutoSelect2> produtosDTO = new ArrayList<>();
    Pageable pageable = PageRequest.of(Integer.valueOf(pagina), 10);
    Page page = entradaProdutoRepository.filtrarProdutosSelect(descricao, pageable);
    if (page.hasContent()) {
      List<Produto> produtos = page.getContent();
      resultSelectAutomoveis.setTotalItens(page.getTotalElements());
      for (Produto produto : produtos) {
        produtoDTO.setId(produto.getId().toString());
        produtoDTO.setText(produto.getCodigoBarra() + " " + produto.getDescricaoProduto() + " " + produto.getCodProduto());
        produtosDTO.add(produtoDTO);
        produtoDTO = new ProdutoSelect2();
      }
      resultSelectAutomoveis.setItems(produtosDTO);
    }
    return resultSelectAutomoveis;
  }

  public BigDecimal totalEntrada(Integer quantidade, BigDecimal valorCusto) {
    BigDecimal itemCost = valorCusto.multiply(new BigDecimal(quantidade));
    BigDecimal totalCost = new BigDecimal(BigInteger.ZERO);
    return totalCost.add(itemCost);
  }
}
