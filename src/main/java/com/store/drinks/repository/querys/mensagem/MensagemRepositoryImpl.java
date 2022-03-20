package com.store.drinks.repository.querys.mensagem;

import com.store.drinks.entidade.dto.Usuariodto;
import com.store.drinks.repository.util.Multitenancy;
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
}
