
package com.store.drinks.repository.querys.cliente.sistema;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class ClienteSistemaRepositoryImpl implements ClienteSistemaRepositoryCustom {
  
  @PersistenceContext
  private EntityManager manager;
  
  
}
