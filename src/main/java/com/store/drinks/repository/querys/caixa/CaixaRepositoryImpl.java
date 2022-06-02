package com.store.drinks.repository.querys.caixa;

import com.store.drinks.entidade.Usuario;
import com.store.drinks.entidade.dto.Caixadto;
import com.store.drinks.repository.util.Multitenancy;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;

public class CaixaRepositoryImpl implements CaixaRepositoryCustom {

  @PersistenceContext
  private EntityManager manager;

  @Autowired
  private Multitenancy multitenancy;

  @Override
  public Optional<Caixadto> findByAbertoTrueAndUsuario(Usuario usuario) {
    try {
      StringBuilder sql = new StringBuilder();
      sql.append("select * from caixa ");
      sql.append("where caixa.aberto = true ");
      sql.append("and caixa.tenant = ").append("'").append(multitenancy.getTenantValue()).append("' ");
      sql.append("and caixa.usuario_id = ").append(usuario.getId());
      Query query = manager.createNativeQuery(sql.toString(), "Caixadto");
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
      Query query = manager.createNativeQuery(sql.toString(), "Caixadto");
      return Optional.ofNullable((Caixadto) query.getSingleResult());
    } catch (NoResultException ex) {
      return Optional.empty();
    }
  }
}
