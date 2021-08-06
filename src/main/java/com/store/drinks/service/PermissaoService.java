
package com.store.drinks.service;

import com.store.drinks.entidade.Permissao;
import com.store.drinks.repository.PermissaoRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class PermissaoService {
    
    @Autowired
    private PermissaoRepository permissaoRepository;
    
    public List<Permissao> todas() {
        return permissaoRepository.findAll(Sort.unsorted().descending());
    }
    
}
