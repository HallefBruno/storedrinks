package com.store.drinks.repository.querys.venda;

import com.store.drinks.entidade.ItensVenda;
import com.store.drinks.entidade.Usuario;
import com.store.drinks.entidade.Venda;
import com.store.drinks.repository.util.RowsUtil;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class VendaRepositoryImpl implements VendaRepositoryCustom {
  
  @PersistenceContext
  private EntityManager manager;
  
  @Autowired
  private RowsUtil rowsUtil;

  @Override
  public Page<Venda> buscarVendasParaCancelar(Pageable pageable) {
    try {
      CriteriaBuilder cb = manager.getCriteriaBuilder();
      CriteriaQuery<Venda> query = cb.createQuery(Venda.class);
      Root<Venda> venda = query.from(Venda.class);
      Join<Venda, ItensVenda> itensVenda = (Join) venda.fetch("itensVendas");
      Join<Venda, Usuario> usuario = (Join) venda.fetch("usuario");
      List<Predicate> predicates = new ArrayList<>();
      
      predicates.add(cb.gt(itensVenda.get("quantidade"), 0));
      predicates.add(cb.isTrue(usuario.get("proprietario")));
      
      query.select(venda);
      query.where(predicates.toArray(Predicate[]::new));
      TypedQuery<Venda> typedQuery = manager.createQuery(query);
      Long count = rowsUtil.paginacao(cb, query, venda, manager, typedQuery, pageable);
      return new PageImpl<>(typedQuery.getResultList(), pageable, count);
    } catch (NoResultException ex) {
      return Page.empty();
    }
  }

}