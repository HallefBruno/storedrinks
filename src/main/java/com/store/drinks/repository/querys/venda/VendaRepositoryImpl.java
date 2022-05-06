package com.store.drinks.repository.querys.venda;

import com.store.drinks.entidade.dto.venda.CancelarVendadto;
import com.store.drinks.repository.util.Multitenancy;
import com.store.drinks.service.UsuarioService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class VendaRepositoryImpl implements VendaRepositoryCustom {
  
  @PersistenceContext
  private EntityManager manager;
  
  @Autowired
  private Multitenancy multitenancy;
  
  @Autowired
  private UsuarioService usuarioService;

  @Override
  public Page<CancelarVendadto> getVendasCancelar(Pageable pageable) {
    StringBuilder sqlCount = new StringBuilder();
    StringBuilder sqlQuery = new StringBuilder();
    Boolean isUsuarioProprietario = usuarioService.usuarioLogado().getProprietario();
    sqlCount.append("select count(*) as numero_registro ");
    sqlCount.append("from( ");
    sqlQuery.append("select ");
    sqlQuery.append("mc.id as movimentacao_caixa_id, ");
    sqlQuery.append("mc.tenant, ");
    sqlQuery.append("mc.valor_recebido, ");
    sqlQuery.append("mc.valor_troco, ");
    sqlQuery.append("mc.caixa_id, ");
    sqlQuery.append("mc.venda_id, ");
    sqlQuery.append("v.valor_total_venda, ");
    sqlQuery.append("v.data_hora_venda, ");
    sqlQuery.append("u.id as usuario_id, ");
    sqlQuery.append("u.nome, ");
    sqlQuery.append("u.email ");
    sqlQuery.append("from movimentacao_caixa mc ");
    sqlQuery.append("inner join venda v on v.id  = mc.venda_id ");
    sqlQuery.append("inner join usuario u on u.id  = v.usuario_id ");
    sqlQuery.append("where u.id = ").append(usuarioService.usuarioLogado().getId()).append(" ");
    sqlQuery.append("and u.tenant = '").append(multitenancy.getTenantValue()).append("' ");
    if(isUsuarioProprietario) {
      sqlQuery.append("union ");
      sqlQuery.append("select ");
      sqlQuery.append("mc.id as movimentacao_caixa_id, ");
      sqlQuery.append("mc.tenant, ");
      sqlQuery.append("mc.valor_recebido, ");
      sqlQuery.append("mc.valor_troco, ");
      sqlQuery.append("mc.caixa_id, ");
      sqlQuery.append("mc.venda_id, ");
      sqlQuery.append("v.valor_total_venda, ");
      sqlQuery.append("v.data_hora_venda, ");
      sqlQuery.append("u.id as usuario_id, ");
      sqlQuery.append("u.nome, ");
      sqlQuery.append("u.email ");
      sqlQuery.append("from movimentacao_caixa mc ");
      sqlQuery.append("inner join venda v on v.id  = mc.venda_id ");
      sqlQuery.append("inner join usuario u on u.id  = v.usuario_id ");
      sqlQuery.append("where u.tenant = '").append(multitenancy.getTenantValue()).append("' ");
    }
    sqlCount.append(sqlQuery);
    sqlCount.append(")count ");
    
    Long count = Long.parseLong(manager.createNativeQuery(sqlCount.toString()).getSingleResult().toString());
    
    Query query = manager.createNativeQuery(sqlQuery.toString(), "CancelarVendadto");
    int paginaAtual = pageable.getPageNumber();
    int totalRegistrosPorPagina = pageable.getPageSize();
    int primeiroRegistro = paginaAtual * totalRegistrosPorPagina;
    query.setFirstResult(primeiroRegistro);
    query.setMaxResults(totalRegistrosPorPagina);
    return new PageImpl<>(query.getResultList(), pageable, count);
  }

}