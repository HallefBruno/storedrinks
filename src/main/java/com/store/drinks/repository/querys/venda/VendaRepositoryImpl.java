package com.store.drinks.repository.querys.venda;

import com.store.drinks.entidade.dto.venda.CancelarVendadto;
import com.store.drinks.entidade.dto.venda.ItensVendaCancelardto;
import com.store.drinks.repository.util.Multitenancy;
import com.store.drinks.service.UsuarioService;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
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
    Boolean isUsuarioProprietario = UsuarioService.usuarioLogado().getProprietario();
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
    sqlQuery.append("inner join caixa cx on mc.caixa_id  = cx.id ");
    sqlQuery.append("where u.id = ").append(UsuarioService.usuarioLogado().getId()).append(" ");
    sqlQuery.append("and cx.aberto = true ");
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
      sqlQuery.append("inner join caixa cx on mc.caixa_id  = cx.id ");
      sqlQuery.append("where u.tenant = '").append(multitenancy.getTenantValue()).append("' ");
      sqlQuery.append("and cx.aberto = true ");
    }
    sqlCount.append(sqlQuery);
    sqlCount.append(")count ");
    
    Long count = Long.valueOf(manager.createNativeQuery(sqlCount.toString()).getSingleResult().toString());
    
    Query query = manager.createNativeQuery(sqlQuery.toString(), "CancelarVendadto");
    int paginaAtual = pageable.getPageNumber();
    int totalRegistrosPorPagina = pageable.getPageSize();
    int primeiroRegistro = paginaAtual * totalRegistrosPorPagina;
    query.setFirstResult(primeiroRegistro);
    query.setMaxResults(totalRegistrosPorPagina);
    List<CancelarVendadto> cancelarVendadtos = query.getResultList();
    List<CancelarVendadto> listVendasOrdenada = cancelarVendadtos.stream().sorted(Collections.reverseOrder()).collect(Collectors.toList());
    return new PageImpl<>(listVendasOrdenada, pageable, count);
  }
  
  @Override
  public List<ItensVendaCancelardto> getItensVenda(Long vendaId) {
    StringBuilder sqlQuery = new StringBuilder();
    sqlQuery.append("select iv.id, iv.quantidade, p.descricao_produto ,p.valor_venda, v.valor_total_venda ");
    sqlQuery.append("from itens_venda iv ");
    sqlQuery.append("inner join produto p on(p.id = iv.produto_id) ");
    sqlQuery.append("inner join venda v on (v.id = iv.venda_id) ");
    sqlQuery.append("where iv.tenant = '").append(multitenancy.getTenantValue()).append("' ");
    sqlQuery.append("and v.id = ").append(vendaId);
    Query query = manager.createNativeQuery(sqlQuery.toString(), "ItensVendaCancelardto");
    return query.getResultList();
  }

}