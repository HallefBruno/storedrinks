
package com.store.drinks.service;

import com.store.drinks.entidade.ClienteSistema;
import com.store.drinks.entidade.ValidarCliente;
import com.store.drinks.execption.NegocioException;
import com.store.drinks.repository.ValidarClienteRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class ValidarClienteService {
    
    @Autowired
    private ValidarClienteRepository clienteRepository;
    
    @Autowired
    private ClienteSistemaService clienteSistemaService;
    
    @Transactional
    public void salvar(String cpfCnpj) {
        Optional<ValidarCliente> temCnpj = clienteRepository.findByCpfCnpj(cpfCnpj);
        if(temCnpj.isPresent()) {
            throw new NegocioException("Esse cliente já possui conta!");
        }
        Optional<ClienteSistema> opClienteSistema = clienteSistemaService.buscarPorCpfCnpj(cpfCnpj);
        if(opClienteSistema.isPresent()) {
            ClienteSistema clienteSistema = opClienteSistema.get();
            if(clienteSistema.getPrimeiroAcesso() && clienteSistema.getAcessarTelaCriarLogin()) {
                ValidarCliente validarCliente = new ValidarCliente();
                validarCliente.setCpfCnpj(cpfCnpj);
                validarCliente.setDataValidacao(LocalDateTime.now());
                clienteRepository.save(validarCliente);
            }
        } else {
            throw new NegocioException("Cliente sem permisão para criar conta!");
        }

    }
    
}
