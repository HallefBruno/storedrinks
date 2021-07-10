
package com.store.drinks.service;

import com.store.drinks.entidade.Produto;
import com.store.drinks.entidade.dto.ProdutoSelect2;
import com.store.drinks.entidade.dto.ResultSelectProdutos;
import com.store.drinks.execption.NegocioException;
import com.store.drinks.repository.EntradaProdutoRepository;
import com.store.drinks.repository.ProdutoRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EntradaProdutoService {
    
    @Autowired
    private EntradaProdutoRepository entradaProdutoRepository;
    
    @Autowired
    private ProdutoRepository produtoRepository;

    public List<Produto> todos() {
        return produtoRepository.findAll();
    }
    
    public Produto buscarProdutoPorId(Long id) {
        Optional<Produto> opProduto = produtoRepository.findById(id);
        if(opProduto.isPresent()) {
            return opProduto.get();
        }
        throw new NegocioException("Produto não encontrado");
    }
    
    public Produto buscarProdutoPorCodBarra(String codBarra) {
        if(!StringUtils.isEmpty(codBarra)) {
            Optional<Produto> produtoOptional = produtoRepository.findByCodigoBarra(codBarra);
            if(produtoOptional.isPresent()) {
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
        Pageable pageable =  PageRequest.of(Integer.valueOf(pagina), 10);
        Page page = entradaProdutoRepository.filtrarProdutosSelect(descricao,pageable);
        if(page.hasContent()) {
            List<Produto> automovels = page.getContent();
            resultSelectAutomoveis.setTotalItens(page.getTotalElements());
            for(Produto produto : automovels) {
                produtoDTO.setId(produto.getId().toString());
                produtoDTO.setText(produto.getCodigoBarra()+" "+produto.getDescricaoProduto()+" "+produto.getCodProduto());
                produtosDTO.add(produtoDTO);
                produtoDTO = new ProdutoSelect2();
            }
            resultSelectAutomoveis.setItems(produtosDTO);
        }
        return resultSelectAutomoveis;
    }
}
