
package com.store.drinks.service;

import com.store.drinks.entidade.Produto;
import com.store.drinks.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
}
