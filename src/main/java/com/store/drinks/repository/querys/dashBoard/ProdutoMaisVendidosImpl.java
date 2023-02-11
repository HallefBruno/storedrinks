package com.store.drinks.repository.querys.dashboard;

import com.store.drinks.repository.filtros.ProdutosMaisVendidosFiltro;
import com.store.drinks.entidade.ItensVenda;
import com.store.drinks.entidade.Produto;
import com.store.drinks.entidade.Usuario;
import com.store.drinks.entidade.Venda;
import com.store.drinks.entidade.dto.dashboard.DetalheProdutodto;
import com.store.drinks.entidade.dto.dashboard.DetalheVendadto;
import com.store.drinks.entidade.dto.dashboard.TotalCustodto;
import com.store.drinks.entidade.dto.dashboard.TotalVendadto;
import com.store.drinks.entidade.dto.produtosMaisVendidos.ProdutosMaisVendidosdto;
import com.store.drinks.entidade.wrapper.DataTableWrapper;
import com.store.drinks.repository.util.JpaUtils;
import com.store.drinks.service.UsuarioService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class ProdutoMaisVendidosImpl implements ProdutosMaisVendidosRepositoryCustom {

  @PersistenceContext
  private EntityManager entityManager;

  @Autowired
  private JpaUtils jpaUtils;

  @Override
  public List<ProdutosMaisVendidosdto> listProdutosMaisVendidos(ProdutosMaisVendidosFiltro filters) {
    Usuario usuarioLogado = UsuarioService.usuarioLogado();

    if (Objects.isNull(filters.getUsuarioId())) {
      filters.setUsuarioId(usuarioLogado.getId());
    }

    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Tuple> query = builder.createQuery(Tuple.class);
    Root<ItensVenda> root = query.from(ItensVenda.class);
    Join<ItensVenda, Produto> produto = root.join("produto");
    Join<ItensVenda, Venda> venda = root.join("venda");
    Join<Venda, Usuario> usuario = venda.join("usuario");

    List<Predicate> predicates = new ArrayList<>();
    List<Selection<?>> selections = new ArrayList<>();

    selections.add(builder.sum(root.get("quantidade")).alias("quantidade"));
    selections.add(produto.get("descricaoProduto").alias("descricaoProduto"));
    query.multiselect(selections);

    if (Objects.nonNull(filters) && StringUtils.isNotBlank(filters.getDataInicial()) && StringUtils.isNotBlank(filters.getDataFinal()) && Objects.nonNull(filters.getUsuarioId())) {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault());
      LocalDate dataInicial = LocalDate.parse(filters.getDataInicial(), formatter);
      LocalDate dataFinal = LocalDate.parse(filters.getDataFinal(), formatter);
      predicates.add(builder.between(venda.get("dataVenda"), dataInicial, dataFinal));
      predicates.add(builder.and(builder.equal(usuario.get("id"), filters.getUsuarioId())));
    } else if (Objects.nonNull(filters.getUsuarioId())) {
      predicates.add(builder.and(builder.equal(usuario.get("id"), filters.getUsuarioId())));
      predicates.add(builder.greaterThanOrEqualTo(venda.get("dataVenda"), LocalDate.now()));
    }

    predicates.add(builder.and(builder.equal(root.get("tenant"), usuarioLogado.getClienteSistema().getTenant())));

    query.where(predicates.toArray(Predicate[]::new));

    query.groupBy(produto.get("descricaoProduto"));
    query.orderBy(builder.desc(builder.sum(root.get("quantidade"))));

    TypedQuery<Tuple> typedQuery = entityManager.createQuery(query);
    typedQuery.setMaxResults(50);

    List<Tuple> listTuple = typedQuery.getResultList();
    List<ProdutosMaisVendidosdto> list = jpaUtils.parseTuple(listTuple, ProdutosMaisVendidosdto.class);

    return list;
  }

  @Override
  public DataTableWrapper<DetalheVendadto> listVendasTempoReal(Long idUsuario, Integer draw, Integer start, Integer length) {
    DataTableWrapper<DetalheVendadto> dataTableWrapper = new DataTableWrapper<>();
    Usuario usuarioLogado = UsuarioService.usuarioLogado();

    if (Objects.isNull(idUsuario)) {
      idUsuario = usuarioLogado.getId();
    }

    StringBuilder sqlSelect = new StringBuilder();
    sqlSelect.append(" select distinct v.id as idvenda, v.valor_total_venda, ");
    sqlSelect.append(" (select string_agg(produto.descricao_produto, ', ') as nomeproduto ");
    sqlSelect.append("    from venda ");
    sqlSelect.append("    inner join itens_venda on itens_venda.venda_id = venda.id ");
    sqlSelect.append("    inner join produto on produto.id = itens_venda.produto_id ");
    sqlSelect.append("    where venda.id = v.id), ");
    sqlSelect.append(" (select sum(itens_venda.quantidade) as quantidade from itens_venda ");
    sqlSelect.append("   inner join venda on venda.id = itens_venda.venda_id ");
    sqlSelect.append("   inner join produto on produto.id = itens_venda.produto_id ");
    sqlSelect.append("   where venda.id = v.id), ");
    sqlSelect.append("   v.valor_total_venda as valortotal, usuario.nome as nomevendedor, v.data_hora_venda as datahoravenda  ");

    String sqlCount = "  select count (distinct(v.id)) ";
    String sqlFrom = " from venda v inner join itens_venda on v.id = itens_venda.venda_id inner join produto on produto.id = itens_venda.produto_id inner join usuario on usuario.id = v.usuario_id where v.usuario_id = :idUsuario and v.data_venda = :dataVenda and v.tenant = :tenant ";
    String orderBy = " order by v.data_hora_venda desc ";

    Query query = entityManager.createNativeQuery(sqlSelect.append(sqlFrom).append(orderBy).toString(), "DetalheVendadto");
    Query queryCount = entityManager.createNativeQuery(sqlCount.concat(sqlFrom));

    query.setParameter("idUsuario", idUsuario);
    query.setParameter("dataVenda", LocalDate.now());
    query.setParameter("tenant", usuarioLogado.getClienteSistema().getTenant());
    query.setFirstResult(start);
    query.setMaxResults(length);

    query.getParameters().forEach(q -> queryCount.setParameter(q.getName(), query.getParameterValue(q.getName())));

    Long count = Long.valueOf(queryCount.getSingleResult().toString());

    dataTableWrapper.setData(query.getResultList());
    dataTableWrapper.setRecordsTotal(count);
    dataTableWrapper.setRecordsFiltered(count);
    dataTableWrapper.setDraw(draw);
    dataTableWrapper.setStart(start);

    return dataTableWrapper;
  }

  @Override
  public List<DetalheProdutodto> listDetalheProdutoVendido(Long idVenda) {

    Usuario usuarioLogado = UsuarioService.usuarioLogado();

    if (Objects.isNull(idVenda) || idVenda <= 0) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "idVenda é obrigatório!");
    }

    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Tuple> query = builder.createQuery(Tuple.class);
    Root<ItensVenda> root = query.from(ItensVenda.class);
    Join<ItensVenda, Produto> produto = root.join("produto");
    Join<ItensVenda, Venda> venda = root.join("venda");

    List<Predicate> predicates = new ArrayList<>();
    List<Selection<?>> selections = new ArrayList<>();

    selections.add(root.get("quantidade").alias("quantidade"));
    selections.add(produto.get("descricaoProduto").alias("descricaoProduto"));
    selections.add(produto.get("valorVenda").alias("valorVenda"));
    selections.add(venda.get("valorTotalVenda").alias("valorTotal"));
    query.multiselect(selections);

    predicates.add(builder.equal(root.get("tenant"), usuarioLogado.getClienteSistema().getTenant()));
    predicates.add(builder.and(builder.equal(venda.get("id"), idVenda)));
    query.where(predicates.toArray(Predicate[]::new));
    TypedQuery<Tuple> typedQuery = entityManager.createQuery(query);

    try {
      List<Tuple> listTuple = typedQuery.getResultList();
      List<DetalheProdutodto> list = jpaUtils.parseTuple(listTuple, DetalheProdutodto.class);
      return list;
    } catch (NoResultException ex) {
      return Collections.EMPTY_LIST;
    }
  }

  @Override
  public TotalVendadto totalVenda(Long idUsuario) {
    TotalVendadto totalVendadto = new TotalVendadto(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
    try {
      Usuario usuarioLogado = UsuarioService.usuarioLogado();
      String jpql = " SELECT DISTINCT NEW com.store.drinks.entidade.dto.dashboard.TotalVendadto(COALESCE((SELECT SUM(ven.valorTotalVenda) FROM Venda ven WHERE ven.tenant = :tenant AND day(ven.dataVenda)=day(current_date())), 0) AS totaldia, COALESCE((SELECT SUM(ven.valorTotalVenda) FROM Venda ven WHERE ven.tenant = :tenant AND month(ven.dataVenda)=month(current_date())), 0) as totalMes, COALESCE((SELECT SUM(ven.valorTotalVenda) FROM Venda ven WHERE ven.tenant = :tenant AND year(ven.dataVenda)=year(current_date())), 0) as totalAno) FROM Venda v WHERE v.tenant = :tenant ";
      Map<String, Object> paramaterMap = new HashMap<>();
      paramaterMap.put("tenant", usuarioLogado.getClienteSistema().getTenant());
      if (Objects.nonNull(idUsuario) && idUsuario <= 0) {
        String andUsuario = " AND v.usuario.id = :idUsuario ";
        jpql = jpql.concat(andUsuario);
        paramaterMap.put("idUsuario", usuarioLogado.getId());
      }
      Query query = entityManager.createQuery(jpql);
      for (String key : paramaterMap.keySet()) {
        query.setParameter(key, paramaterMap.get(key));
      }
      totalVendadto = (TotalVendadto) query.getSingleResult();
      return totalVendadto;
    } catch (NoResultException ex) {
      return totalVendadto;
    }
  }

  @Override
  public TotalCustodto totalCusto(Long idUsuario) {
    TotalCustodto totalCustodto = new TotalCustodto(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
    try {
      Usuario usuarioLogado = UsuarioService.usuarioLogado();
      
      Query query = entityManager.createQuery(" select distinct new com.store.drinks.entidade.dto.dashboard.TotalCustodto( coalesce((select sum(produto.valorCusto * itensVendas.quantidade) from ItensVenda itensVendas join itensVendas.venda venda join itensVendas.produto produto where itensVendas.tenant = :tenant and day(venda.dataVenda)=day(current_date())),0) as totalDia,  coalesce((select sum(produto.valorCusto * itensVendas.quantidade) from ItensVenda itensVendas join itensVendas.venda venda join itensVendas.produto produto where itensVendas.tenant = :tenant and month(venda.dataVenda)=month(current_date())),0) as totalMes, coalesce((select sum(produto.valorCusto * itensVendas.quantidade) from ItensVenda itensVendas join itensVendas.venda venda join itensVendas.produto produto where itensVendas.tenant = :tenant and year(venda.dataVenda)=year(current_date())),0) as totalAno) from ItensVenda iv where iv.tenant = :tenant ");
      query.setParameter("tenant", usuarioLogado.getClienteSistema().getTenant());
      
      totalCustodto = (TotalCustodto) query.getSingleResult();
      return totalCustodto;
    } catch (NoResultException ex) { 
      return totalCustodto;
    }
  }

}
