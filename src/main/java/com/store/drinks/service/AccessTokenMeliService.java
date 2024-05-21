
package com.store.drinks.service;

import com.store.drinks.entidade.AccessTokenMeli;
import com.store.drinks.repository.AccessTokenMeliRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccessTokenMeliService {
  
  @Autowired
  private AccessTokenMeliRepository accessTokenMeliRepository;
  
  @Transactional
  public AccessTokenMeli salvar(AccessTokenMeli accessTokenMeli) {
    accessTokenMeli = this.accessTokenMeliRepository.save(accessTokenMeli);
    return accessTokenMeli;
  }
  
}
