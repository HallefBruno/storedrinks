package com.store.drinks.service;

import com.store.drinks.entidade.Produto;
import com.store.drinks.execption.NegocioException;
import com.store.drinks.repository.ProdutoRepository;
import java.util.Optional;
import javax.persistence.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Transactional
    public void salvar(Produto produto) {
        Optional<Produto> findProduto = produtoRepository.findFirstByCodigoBarraOrCodProdutoOrDescricaoProduto(produto.getCodigoBarra(), produto.getCodProduto(), produto.getDescricaoProduto());
        if(findProduto.isPresent()) {
            throw new NegocioException("Esse produto ja foi cadastrado!");
        }
        produtoRepository.save(produto);
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
}
