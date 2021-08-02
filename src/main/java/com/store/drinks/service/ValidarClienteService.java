
package com.store.drinks.service;

import com.store.drinks.repository.ValidarClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ValidarClienteService {
    
    @Autowired
    private ValidarClienteRepository clienteRepository;
    
}
