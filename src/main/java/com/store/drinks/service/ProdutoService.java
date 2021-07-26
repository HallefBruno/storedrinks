package com.store.drinks.service;

import com.store.drinks.entidade.Produto;
import com.store.drinks.entidade.dto.ProdutoSelect2;
import com.store.drinks.entidade.dto.ResultSelectProdutos;
import com.store.drinks.execption.NegocioException;
import com.store.drinks.repository.ProdutoRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.persistence.PersistenceException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Transactional
    public void salvar(Produto produto) {
        produtoRepository.save(produto);
    }

    @Transactional
    public void update(Produto update, Long codigo) {
        if (Objects.isNull(codigo)) {
            throw new NegocioException("Código não pode ser null!");
        }
        Optional<Produto> optionalProdutoAtual = produtoRepository.findById(codigo);
        if (optionalProdutoAtual.isPresent()) {
            Produto atual = optionalProdutoAtual.get();
            BeanUtils.copyProperties(update, atual, "id");
            produtoRepository.save(atual);
        }
    }

    @Transactional
    public void excluir(Produto produto) {
        try {
            produtoRepository.delete(produto);
            produtoRepository.flush();
        } catch (PersistenceException e) {
            throw new NegocioException("Impossível apagar produto");
        }
    }
    
    public ResultSelectProdutos pesquisarProdutosAutoComplete(String descricao, String pagina) {
        ResultSelectProdutos resultSelectAutomoveis = new ResultSelectProdutos();
        List<ProdutoSelect2> produtosDTO = new ArrayList<>();
        Pageable pageable = PageRequest.of(Integer.valueOf(pagina), 10);
        Page page = produtoRepository.filtrarProdutosSelect(descricao, pageable);
        if (page.hasContent()) {
            List<Produto> automovels = page.getContent();
            resultSelectAutomoveis.setTotalItens(page.getTotalElements());
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
            resultSelectAutomoveis.setItems(produtosDTO);
        }
        return resultSelectAutomoveis;
    }
}
