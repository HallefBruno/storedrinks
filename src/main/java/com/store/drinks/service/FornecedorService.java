package com.store.drinks.service;

import com.store.drinks.entidade.Fornecedor;
import com.store.drinks.execption.NegocioException;
import com.store.drinks.repository.FornecedorRepository;
import com.store.drinks.repository.querys.fornecedor.FornecedorFilter;
import com.store.drinks.repository.util.Multitenancy;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class FornecedorService {

  private final FornecedorRepository fornecedorRepository;
  private final Multitenancy multitenancy;

  @Transactional
  public void salvar(Fornecedor fornecedor) {
    fornecedorRepository.verificarExistenciaFornecedor(fornecedor);
    fornecedorRepository.save(fornecedor);
  }
  
  @Transactional
  public void update(Fornecedor update, Long codigo) {
    try {
      if (Objects.isNull(codigo)) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Identificador inválido!");
      }
      update.setId(codigo);
      fornecedorRepository.verificarExistenciaFornecedor(update);
      fornecedorRepository.save(update);
    } catch (ObjectOptimisticLockingFailureException ex) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Houve uma alteração neste fornecesor, faça uma nova busca!");
    }
  }

  @Transactional
  public void excluir(Fornecedor fornecedor) {
    try {
      fornecedorRepository.delete(fornecedor);
      fornecedorRepository.flush();
    } catch (Exception e) {
      throw new NegocioException("Não foi possível excluir o fornecedor!");
    }
  }

  public Page<Fornecedor> filtrar(FornecedorFilter fornecedorFilter, Pageable pageable) {
    return fornecedorRepository.filtrar(fornecedorFilter, pageable);
  }

  public List<Fornecedor> todos() {
    return fornecedorRepository.findByTenantAndAtivoTrue(multitenancy.getTenantValue());
  }

}
