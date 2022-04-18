package com.store.drinks.repository;

import com.store.drinks.entidade.Fornecedor;
import com.store.drinks.repository.querys.fornecedor.FornecedorRepositoryCustom;
import java.util.List;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FornecedorRepository extends JpaRepository<Fornecedor, Long>, FornecedorRepositoryCustom {
  
  @Cacheable("fornecedor")
  List<Fornecedor> findByTenantAndAtivoTrue(String tenant);
  //List<Fornecedor> findAllByTenantAndAtivoTrue(String tenant);
}
