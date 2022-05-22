package com.store.drinks.service;

import com.store.drinks.entidade.Fornecedor;
import com.store.drinks.execption.NegocioException;
import com.store.drinks.repository.FornecedorRepository;
import com.store.drinks.repository.querys.fornecedor.FornecedorFilter;
import com.store.drinks.repository.util.Multitenancy;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    if (Objects.isNull(codigo)) {
      throw new NegocioException("Identificador inválido!");
    }
    update.setId(codigo);
    fornecedorRepository.verificarExistenciaFornecedor(update);
    Optional<Fornecedor> opFornecedor = fornecedorRepository.findById(codigo);
    if (opFornecedor.isPresent()) {
      Fornecedor atual = opFornecedor.get();
      if (!Objects.equals(atual.getVersaoObjeto(), update.getVersaoObjeto())) {
        throw new NegocioException("Houve uma alteração neste fornecedor, faça uma nova busca");
      }
      BeanUtils.copyProperties(update, atual, "id");
      fornecedorRepository.save(atual);
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
