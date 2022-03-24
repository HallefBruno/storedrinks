package com.store.drinks.repository.querys.mensagem;

import com.store.drinks.entidade.dto.Usuariodto;
import com.store.drinks.entidade.dto.mensagem.Mensagemdto;
import com.store.drinks.repository.util.Multitenancy;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.ObjectUtils;

public class MensagemRepositoryImpl implements MensagemRepositoryCustom {
  
  @PersistenceContext
  private EntityManager manager;

  @Autowired
  private Multitenancy multitenancy;
  
  @Override
  public Page<Usuariodto> pesquisarComercioAutoComplete(String comercio, Pageable pageable) {
    StringBuilder sqlUnion = new StringBuilder();
    StringBuilder sqlCount = new StringBuilder();
    String sqlNomeComercio = "";
    if (!StringUtils.isBlank(comercio)) {
      sqlNomeComercio = " and cs.nome_comercio ilike ('".concat("%").concat(comercio).concat("%') ");
    }
    sqlCount.append(" select count(*) as numero_registro ");
    sqlCount.append(" from( ");
    sqlUnion.append(" select u.id, cs.nome_comercio as text, u.nome, u.email as destinatario, cs.tenant ");
    sqlUnion.append(" from cliente_sistema cs ");
    sqlUnion.append(" inner join usuario u on(cs.tenant = u.tenant) ");
    sqlUnion.append(" where u.proprietario = true ").append(sqlNomeComercio);
    sqlUnion.append(" union ");
    sqlUnion.append(" select u.id, cs.nome_comercio as text, u.nome, u.email as destinatario, cs.tenant ");
    sqlUnion.append(" from cliente_sistema cs ");
    sqlUnion.append(" inner join usuario u on(cs.tenant = u.tenant) ");
    sqlUnion.append(" where u.tenant = '").append(multitenancy.getTenantValue()).append("' ");
    sqlCount.append(sqlUnion);
    sqlCount.append(" )count ");

    Long count = null;
    Query longQuery = manager.createNativeQuery(sqlCount.toString());
    Object singleResult = longQuery.getSingleResult();
    if (!ObjectUtils.isEmpty(singleResult)) {
      count = Long.parseLong(singleResult.toString());
    }
    Query query = manager.createNativeQuery(sqlUnion.toString(), "Usuariodto");
    int paginaAtual = pageable.getPageNumber();
    int totalRegistrosPorPagina = pageable.getPageSize();
    int primeiroRegistro = paginaAtual * totalRegistrosPorPagina;
    query.setFirstResult(primeiroRegistro);
    query.setMaxResults(totalRegistrosPorPagina);
    return new PageImpl<>(query.getResultList(), pageable, count);
  }
  
  @Override
  public Boolean existemMensagensNaoLidas(String tenant, Long usuarioId) {
    StringBuilder sql = new StringBuilder();
    sql.append(" select men.notificado from mensagem men ");
    sql.append(" where men.tenant = '").append(tenant).append("' ");
    sql.append(" and men.usuario_id = ").append(usuarioId);
    Query query = manager.createNativeQuery(sql.toString());
    List<Boolean> booleans = (List<Boolean>) query.getResultList();
    return booleans.stream().anyMatch(bool -> bool.equals(Boolean.FALSE));
  }

  @Override
  public Page<Mensagemdto> findAllByLida(Boolean lida,String remetente, Pageable pageable) {
    StringBuilder sql = new StringBuilder();
    StringBuilder sqlCount = new StringBuilder();
    sqlCount.append(" select count(*) as numero_registro ");
    sqlCount.append(" from( ");
    sql.append(" select me.*, cs.nome_comercio as remetente ");
    sql.append(" from mensagem me ");
    sql.append(" inner join usuario u on (u.id = me.usuario_id) ");
    sql.append(" inner join cliente_sistema cs on (cs.tenant = u.tenant) ");
    sql.append(" where me.destinatario = '").append(remetente).append("' ");
    sql.append(" and me.tenant = '").append(multitenancy.getTenantValue()).append("' ");
    sql.append(" and me.lida = false ");
    sql.append(" order by me.data_hora_mensagem_recebida desc ");
    sqlCount.append(sql);
    sqlCount.append(" )count ");
    
    Long count = null;
    Query longQuery = manager.createNativeQuery(sqlCount.toString());
    Object singleResult = longQuery.getSingleResult();
    if (!ObjectUtils.isEmpty(singleResult)) {
      count = Long.parseLong(singleResult.toString());
    }
    Query query = manager.createNativeQuery(sql.toString(), "Mensagemdto");
    int paginaAtual = pageable.getPageNumber();
    int totalRegistrosPorPagina = pageable.getPageSize();
    int primeiroRegistro = paginaAtual * totalRegistrosPorPagina;
    query.setFirstResult(primeiroRegistro);
    query.setMaxResults(totalRegistrosPorPagina);
    return new PageImpl<>(query.getResultList(), pageable, count);
    
  }
}
