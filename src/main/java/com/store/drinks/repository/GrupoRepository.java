
package com.store.drinks.repository;

import com.store.drinks.entidade.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GrupoRepository extends JpaRepository<Grupo, Long>{
    
}
