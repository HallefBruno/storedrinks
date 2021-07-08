
package com.store.drinks.repository;

import com.store.drinks.entidade.EntradaProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntradaProdutoRepository extends JpaRepository<EntradaProduto, Long> {
    
}
