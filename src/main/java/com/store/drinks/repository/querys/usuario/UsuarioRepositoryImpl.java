package com.store.drinks.repository.querys.usuario;

import com.store.drinks.entidade.ClienteSistema;
import com.store.drinks.entidade.Usuario;
import com.store.drinks.repository.util.RowsUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class UsuarioRepositoryImpl implements UsuarioRepositoryCustom {

  @PersistenceContext
  private EntityManager manager;

  @Autowired
  private RowsUtil rowsUtil;

  @Override
  public Optional<Usuario> findByUserLogin(String email) {
    return manager
      .createQuery("select u from Usuario u where lower(u.email) = lower(:email) and u.ativo = true", Usuario.class)
      .setParameter("email", email).getResultList().stream().findFirst();
  }

  @Override
  public List<String> permissoes(Usuario usuario) {
    return manager.createQuery("select distinct p.nome from Usuario u inner join u.grupos g inner join g.permissoes p where u = :usuario", String.class)
      .setParameter("usuario", usuario)
      .getResultList();
  }
  
  public Page<Usuario> pesquisarComercioAutoComplete1(String comercio, Pageable pageable) {
    CriteriaBuilder cb = manager.getCriteriaBuilder();
    EntityGraph graph = manager.getEntityGraph("graph.Usuario.clienteSistema");
    CriteriaQuery<Usuario> query = cb.createQuery(Usuario.class);
    Root<Usuario> usuario = query.from(Usuario.class);
    Join<Usuario, ClienteSistema> clienteSistema = (Join) usuario.fetch("clienteSistema");
    List<Predicate> predicates = new ArrayList<>();
    Predicate predicate;

    if (!StringUtils.isBlank(comercio)) {
      predicate = cb.like(cb.upper(clienteSistema.get("nomeComercio")), "%" + comercio.toUpperCase() + "%");
      predicates.add(predicate);
    }

    predicate = cb.equal(usuario.get("proprietario"), Boolean.TRUE);
    predicates.add(predicate);

    query.select(usuario);
    query.where(predicates.toArray(Predicate[]::new));
    TypedQuery<Usuario> typedQuery = manager.createQuery(query);
    typedQuery.setHint("javax.persistence.fetchgraph", graph);
    Long count = rowsUtil.paginacao(cb, query, usuario, manager, typedQuery, pageable);
    return new PageImpl<>(typedQuery.getResultList(), pageable, count);

  }
}
