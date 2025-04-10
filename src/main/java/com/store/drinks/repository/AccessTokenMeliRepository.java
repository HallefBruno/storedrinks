
package com.store.drinks.repository;

import com.store.drinks.entidade.AccessTokenMeli;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessTokenMeliRepository extends JpaRepository<AccessTokenMeli, Long> {
  
}
