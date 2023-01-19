package com.store.drinks.service;

import com.store.drinks.entidade.ValidarCliente;
import com.store.drinks.repository.ValidarClienteRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ValidarClienteService {
  
  private final ValidarClienteRepository validarClienteRepository;
  
  @Transactional
  public void salvar(ValidarCliente validarCliente) {
    validarClienteRepository.save(validarCliente);
  }
  
  public Optional<ValidarCliente> findByCpfCnpj(String cpfCnpj) {
    return validarClienteRepository.findByCpfCnpj(cpfCnpj);
  }
  
}
