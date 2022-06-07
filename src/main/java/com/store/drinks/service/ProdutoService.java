package com.store.drinks.service;

import com.store.drinks.entidade.Produto;
import com.store.drinks.entidade.dto.ProdutoSelect2;
import com.store.drinks.entidade.dto.ResultSelectProdutos;
import com.store.drinks.repository.ProdutoRepository;
import com.store.drinks.repository.util.Multitenancy;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ProdutoService {

  private final ProdutoRepository produtoRepository;
  private final Multitenancy multitenancy;

  @Transactional
  public void salvar(Produto produto) {
    produtoRepository.verificarExistenciaProduto(produto);
    produtoRepository.save(produto);
  }

  @Transactional
  public void update(Produto update, Long codigo) {
    try {
      if (Objects.isNull(codigo)) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Identificador inválido!");
      }
      update.setId(codigo);
      produtoRepository.verificarExistenciaProduto(update);
      produtoRepository.save(update);
    } catch (ObjectOptimisticLockingFailureException ex) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Houve uma alteração neste produto, faça uma nova busca!");
    }
  }
  
  @Transactional
  public void updateQuantidadeProdutoEstoque(final String codBarra, final Integer quantidade) {
    produtoRepository.findByProdutoForUpdate(codBarra, multitenancy.getTenantValue())
    .ifPresent(produto -> {
      int quantidadeAtual = produto.getQuantidade();
      if(quantidadeAtual >= quantidade) {
        quantidadeAtual -= quantidade;
        produto.setQuantidade(quantidadeAtual);
        produtoRepository.saveAndFlush(produto);
        return;
      }
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Você não possui essa quantidade em estoque!");
    });
  }

  @Transactional
  public void excluir(Produto produto) {
    try {
      produtoRepository.delete(produto);
      produtoRepository.flush();
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não foi possível excluir o produto!");
    }
  }

  public ResultSelectProdutos pesquisarProdutosAutoComplete(String descricao, String pagina) {
    ResultSelectProdutos resultSelectProdutos = new ResultSelectProdutos();
    List<ProdutoSelect2> produtosDTO = new ArrayList<>();
    Pageable pageable = PageRequest.of(Integer.valueOf(pagina), 10);
    Page<Produto> page = produtoRepository.filtrarProdutosSelect(descricao, pageable);
    if (page.hasContent()) {
      List<Produto> automovels = page.getContent();
      resultSelectProdutos.setTotalItens(page.getTotalElements());
      for (Produto produto : automovels) {
        ProdutoSelect2 produtoSelect2 = ProdutoSelect2.builder()
          .id(produto.getId().toString())
          .text(produto.getDescricaoProduto())
          .descricaoProduto(produto.getDescricaoProduto())
          .quantidade(produto.getQuantidade().toString())
          .codBarra(produto.getCodigoBarra())
          .codProduto(produto.getCodProduto())
          .valorCusto(produto.getValorCusto().toString())
          .valorVenda(produto.getValorVenda().toString()).build();
        produtosDTO.add(produtoSelect2);
      }
      resultSelectProdutos.setItems(produtosDTO);
    }
    return resultSelectProdutos;
  }
}
