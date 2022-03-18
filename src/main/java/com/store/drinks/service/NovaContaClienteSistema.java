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
public class NovaContaClienteSistema {

  @Autowired
  private ValidarClienteRepository clienteRepository;

  @Autowired
  private ClienteSistemaService clienteSistemaService;

  @Transactional
  public void salvaValidarCliente(String cpfCnpj) {
    Optional<ValidarCliente> temCnpj = clienteRepository.findByCpfCnpj(cpfCnpj);
    if (temCnpj.isPresent() && temCnpj.get().getContaCriada()) {
      throw new NegocioException("Esse cliente já possui conta!");
    } else if (!temCnpj.isPresent()) {
      verificaSeClienteEstaCadastrado(cpfCnpj);
    }
  }

  private void verificaSeClienteEstaCadastrado(String cpfCnpj) {
    Optional<ClienteSistema> opClienteSistema = clienteSistemaService.buscarPorCpfCnpj(cpfCnpj);
    if (opClienteSistema.isPresent()) {
      ClienteSistema clienteSistema = opClienteSistema.get();
      if (clienteSistema.getPrimeiroAcesso() && clienteSistema.getAcessarTelaCriarLogin()) {
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
