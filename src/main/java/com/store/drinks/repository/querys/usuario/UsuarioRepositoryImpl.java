package com.store.drinks.repository.querys.usuario;

import com.store.drinks.entidade.ClienteSistema;
import com.store.drinks.entidade.Usuario;
import com.store.drinks.repository.util.RowsUtil;
import com.store.drinks.service.UsuarioService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.ObjectUtils;

@Slf4j
public class UsuarioRepositoryImpl implements UsuarioRepositoryCustom {

  @PersistenceContext
  private EntityManager entityManager;

  @Autowired
  private RowsUtil rowsUtil;

  @Override
  public Optional<Usuario> findByUserLogin(String email) {
    return entityManager
      .createQuery("select u from Usuario u where lower(u.email) = lower(:email) and u.ativo = true", Usuario.class)
      .setParameter("email", email).getResultList().stream().findFirst();
  }

  @Override
  public List<String> permissoes(Usuario usuario) {
    return entityManager.createQuery("select distinct p.nome from Usuario u inner join u.grupos g inner join g.permissoes p where u = :usuario", String.class)
      .setParameter("usuario", usuario)
      .getResultList();
  }
  
  public Page<Usuario> pesquisarComercioAutoComplete1(String comercio, Pageable pageable) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    EntityGraph graph = entityManager.getEntityGraph("graph.Usuario.clienteSistema");
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
    TypedQuery<Usuario> typedQuery = entityManager.createQuery(query);
    typedQuery.setHint("javax.persistence.fetchgraph", graph);
    Long count = rowsUtil.paginacao(cb, query, usuario, entityManager, typedQuery, pageable);
    return new PageImpl<>(typedQuery.getResultList(), pageable, count);

  }
  
  @Override
  public Boolean permiteCriarUsuario() {
    ClienteSistema clienteSistema = UsuarioService.usuarioLogado().getClienteSistema();
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
    Root<Usuario> root = criteriaQuery.from(Usuario.class);
    root.join("clienteSistema");
    criteriaQuery.select(criteriaBuilder.count(root));
    criteriaQuery.where(criteriaBuilder.equal(root.get("clienteSistema").get("tenant"), clienteSistema.getTenant()));
    Long qtdUsuarioRegistrados = entityManager.createQuery(criteriaQuery).getSingleResult();
    return qtdUsuarioRegistrados < clienteSistema.getQtdUsuario();
  }
  
  @Override
  public Boolean existeEmail(String email) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createQuery(Tuple.class);
    Root<Usuario> root = criteriaQuery.from(Usuario.class);
    criteriaQuery.distinct(true);
    criteriaQuery.multiselect(root.get("email").alias("email"));
    criteriaQuery.where(criteriaBuilder.like(criteriaBuilder.upper(root.get("email")), "%"+email.toUpperCase()+"%"));
    List<Tuple> listTuple = entityManager.createQuery(criteriaQuery).getResultList();
    
    if(ObjectUtils.isEmpty(listTuple)) {
      return Boolean.FALSE;
    } else if (listTuple.size() > 1) {
      log.info("Email repetido "+listTuple.size());
    }
    return Boolean.TRUE;
  }
  
  @Override
  public Boolean existeTelefone(String telefone) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createQuery(Tuple.class);
    Root<Usuario> root = criteriaQuery.from(Usuario.class);
    criteriaQuery.distinct(true);
    criteriaQuery.multiselect(root.get("telefone").alias("telefone"));
    criteriaQuery.where(criteriaBuilder.equal(root.get("telefone"), telefone));
    List<Tuple> listTuple = entityManager.createQuery(criteriaQuery).getResultList();
    
    if(ObjectUtils.isEmpty(listTuple)) {
      return Boolean.FALSE;
    } else if (listTuple.size() > 1) {
      log.info("Telefone repetido "+listTuple.size());
    }
    return Boolean.TRUE;
  }
  
//  @Override
//  public Optional<Boolean> findByEmail(String email) {
//    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
//    CriteriaQuery<Boolean> criteriaQuery = criteriaBuilder.createQuery(Boolean.class);
//    Root<Usuario> root = criteriaQuery.from(Usuario.class);
//    criteriaQuery.select(criteriaBuilder.selectCase().when(criteriaBuilder.like(criteriaBuilder.upper(root.get("email")), email.toUpperCase()), Boolean.TRUE).otherwise(Boolean.FALSE).as(Boolean.class));
//    TypedQuery<Boolean> typedQuery = entityManager.createQuery(criteriaQuery);
//    typedQuery.setMaxResults(1);
//    Boolean b = typedQuery.getSingleResult();
//    return Optional.of(b);
//  }

  
}
