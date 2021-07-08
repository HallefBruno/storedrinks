
package com.store.drinks.service;

import com.store.drinks.entidade.Produto;
import com.store.drinks.repository.EntradaProdutoRepository;
import com.store.drinks.repository.ProdutoRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
    
}
