
package com.store.drinks.service;

import com.store.drinks.entidade.PermissaoUsadaFront;
import com.store.drinks.repository.PermissaoUsadaFrontRepository;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class PermissaoUsadaFrontService {
  
  private final PermissaoUsadaFrontRepository permissaoUsadaFrontRepository;
  
  @Transactional
  public void salvar(PermissaoUsadaFront permissaoUsadaFront) {
    permissaoUsadaFrontRepository.save(permissaoUsadaFront);
  }
  
  @Transactional
  public void update(PermissaoUsadaFront permissaoUsadaFront, Long codigo) {
    if (Objects.isNull(codigo)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Identificador inválido!");
    }
    permissaoUsadaFront.setId(codigo);
    permissaoUsadaFrontRepository.save(permissaoUsadaFront);
  }
  
  @Transactional
  public void excluir(PermissaoUsadaFront permissaoUsadaFront) {
    try {
      permissaoUsadaFrontRepository.delete(permissaoUsadaFront);
      permissaoUsadaFrontRepository.flush();
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Não foi possível excluir o fornecedor!");
    }
  }
  
  public List<PermissaoUsadaFront> todos() {
    return permissaoUsadaFrontRepository.findAll();
  }
  
}
