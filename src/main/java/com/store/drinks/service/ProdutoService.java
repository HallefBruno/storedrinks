package com.store.drinks.service;

import com.store.drinks.entidade.Produto;
import com.store.drinks.execption.NegocioException;
import com.store.drinks.repository.ProdutoRepository;
import java.util.Objects;
import java.util.Optional;
import javax.persistence.PersistenceException;
import org.springframework.beans.BeanUtils;
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
}
