package com.store.drinks.service;

import com.store.drinks.entidade.Fornecedor;
import com.store.drinks.repository.FornecedorRepository;
import com.store.drinks.repository.querys.fornecedor.FornecedorFilter;
import com.store.drinks.repository.util.Multitenancy;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FornecedorService {
    
    @Autowired
    private FornecedorRepository fornecedorRepository;
    
    @Autowired
    private Multitenancy multitenancy;
    
    @Transactional
    public void salvar(Fornecedor fornecedor) {
        fornecedorRepository.verificarExistenciaFornecedor(fornecedor);
        fornecedorRepository.save(fornecedor);
    }
    
    public Page<Fornecedor> filtrar(FornecedorFilter fornecedorFilter, Pageable pageable) {
        return fornecedorRepository.filtrar(fornecedorFilter, pageable);
    }
    
    public List<Fornecedor> todos() {
        return fornecedorRepository.findByTenantAndAtivoTrue(multitenancy.getTenantValue());
    }
    
}
