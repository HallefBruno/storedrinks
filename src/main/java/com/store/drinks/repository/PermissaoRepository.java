
package com.store.drinks.repository;

import com.store.drinks.entidade.Permissao;
import java.util.List;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissaoRepository extends JpaRepository<Permissao, Long> {
    
  @Cacheable("permissoes")
  @Override
  List<Permissao> findAll();
  
}
