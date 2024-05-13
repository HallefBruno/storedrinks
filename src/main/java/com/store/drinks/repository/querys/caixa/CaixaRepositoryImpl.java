package com.store.drinks.repository.querys.caixa;

import com.store.drinks.entidade.Caixa;
import com.store.drinks.entidade.MovimentacaoCaixa;
import com.store.drinks.entidade.Usuario;
import com.store.drinks.entidade.dto.caixa.Caixadto;
import com.store.drinks.entidade.dto.caixa.DetalheSangriadto;
import com.store.drinks.repository.util.JpaUtils;
import com.store.drinks.repository.util.Multitenancy;
import com.store.drinks.service.UsuarioService;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;

public class CaixaRepositoryImpl implements CaixaRepositoryCustom {

  @PersistenceContext
  private EntityManager entityManager;

  @Autowired
  private Multitenancy multitenancy;
  
  @Autowired
  private JpaUtils jpaUtils;
  
  @Override
  public Optional<Caixadto> findByAbertoTrueAndUsuario(Usuario usuario) {
    try {
      StringBuilder sql = new StringBuilder();
      sql.append("select * from caixa ");
      sql.append("where caixa.aberto = true ");
      sql.append("and caixa.tenant = ").append("'").append(multitenancy.getTenantValue()).append("' ");
      sql.append("and caixa.usuario_id = ").append(usuario.getId());
      Query query = entityManager.createNativeQuery(sql.toString(), "Caixadto");
      return Optional.ofNullable((Caixadto) query.getSingleResult());
    } catch (NoResultException ex) {
      return Optional.empty();
    }
  }

  @Override
  public Optional<Caixadto> findByAbertoTrueAndUsuarioId(Long id) {
    try {
      StringBuilder sql = new StringBuilder();
      sql.append("select * from caixa ");
      sql.append("where caixa.aberto = true ");
      sql.append("and caixa.tenant = ").append("'").append(multitenancy.getTenantValue()).append("' ");
      sql.append("and caixa.usuario_id = ").append(id);
      Query query = entityManager.createNativeQuery(sql.toString(), "Caixadto");
      return Optional.ofNullable((Caixadto) query.getSingleResult());
    } catch (NoResultException ex) {
      return Optional.empty();
    }
  }
  
  @Override
  public List<DetalheSangriadto> getListdetalheSangria(Long usuarioId) {
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Tuple> query = builder.createQuery(Tuple.class);
    Root<MovimentacaoCaixa> root = query.from(MovimentacaoCaixa.class);
    Join<MovimentacaoCaixa, Caixa> caixa = root.join("caixa");
    Join<MovimentacaoCaixa, Usuario> usuario = root.join("usuario");
    List<Predicate> predicates = new ArrayList<>();
    
    query.multiselect(
root.get("valorTroco").alias("valor"),
root.get("dataMovimentacao").alias("dataSangria"),
usuario.get("nome").alias("nomeUsuario")
    );
    
    predicates.add(builder.equal(root.get("tenant"), multitenancy.getTenantValue()));
    predicates.add(builder.and(builder.equal(root.get("recolhimento"), true)));
    predicates.add(builder.and(builder.equal(caixa.get("aberto"), true)));
    Predicate predicateUsuario = builder.and(builder.equal(usuario.get("id"), UsuarioService.usuarioLogado().getId()));
    
    if(Objects.nonNull(usuarioId) && usuarioId > 0) {
      predicateUsuario = builder.and(builder.equal(usuario.get("id"), usuarioId));
    }
    
    predicates.add(predicateUsuario);
    
    query.where(
      predicates.toArray(Predicate[]::new)
    );
    
    List<Tuple> listTuple = entityManager.createQuery(query).getResultList();
	List<DetalheSangriadto> detalhesSangria = jpaUtils.parseTuple(listTuple,DetalheSangriadto.class);
    return detalhesSangria;
  }
  
}
