
package com.store.drinks.service;

import com.store.drinks.entidade.ClienteSistema;
import com.store.drinks.repository.ClienteSistemaRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteSistemaService {
    
    @Autowired
    private ClienteSistemaRepository clienteSistemaRepository;
    
    @PreAuthorize("hasRole('SUPER_USER')")
    @Transactional
    public void salvar(ClienteSistema clienteSistema) {
        clienteSistema.setDataCadastro(LocalDateTime.now());
        clienteSistema.setDataAtualizacao(clienteSistema.getDataCadastro());
        clienteSistemaRepository.save(clienteSistema);
    }
    
    public Optional<ClienteSistema> buscarPorCpfCnpj(String cpfCnpj) {
        return clienteSistemaRepository.findByCpfCnpj(cpfCnpj);
    }
}
