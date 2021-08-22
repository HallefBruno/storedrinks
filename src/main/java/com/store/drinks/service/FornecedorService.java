package com.store.drinks.service;

import com.store.drinks.entidade.Fornecedor;
import com.store.drinks.entidade.Produto;
import com.store.drinks.execption.NegocioException;
import com.store.drinks.repository.FornecedorRepository;
import com.store.drinks.repository.querys.fornecedor.FornecedorFilter;
import com.store.drinks.repository.util.Multitenancy;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.BeanUtils;
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
    
    @Transactional
    public void update(Fornecedor update, Long codigo) {
        if (Objects.isNull(codigo)) {
            throw new NegocioException("Código não pode ser null!");
        }
        update.setId(codigo);
        fornecedorRepository.verificarExistenciaFornecedor(update);
        Optional<Fornecedor> opFornecedor = fornecedorRepository.findById(codigo);
        if (opFornecedor.isPresent()) {
            Fornecedor atual = opFornecedor.get();
//            if(!Objects.equals(atual.getVersaoObjeto(), update.getVersaoObjeto())) {
//                throw new NegocioException("Erro de concorrência. Esse fornecedor já foi alterado anteriormente.");
//            }
            BeanUtils.copyProperties(update, atual, "id");
            fornecedorRepository.save(atual);
        }
    }
    
    public Page<Fornecedor> filtrar(FornecedorFilter fornecedorFilter, Pageable pageable) {
        return fornecedorRepository.filtrar(fornecedorFilter, pageable);
    }
    
    public List<Fornecedor> todos() {
        return fornecedorRepository.findByTenantAndAtivoTrue(multitenancy.getTenantValue());
    }
    
}
