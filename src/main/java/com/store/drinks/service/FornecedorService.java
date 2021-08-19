package com.store.drinks.service;

import com.store.drinks.entidade.Fornecedor;
import com.store.drinks.repository.FornecedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FornecedorService {
    
    @Autowired
    private FornecedorRepository fornecedorRepository;
    
    @Transactional
    public void salvar(Fornecedor fornecedor) {
        fornecedorRepository.verificarExistenciaFornecedor(fornecedor);
        fornecedorRepository.save(fornecedor);
    }
    
}
