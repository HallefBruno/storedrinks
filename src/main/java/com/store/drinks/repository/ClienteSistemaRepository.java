
package com.store.drinks.repository;

import com.store.drinks.entidade.ClienteSistema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteSistemaRepository extends JpaRepository<ClienteSistema, Long> {
    
}
