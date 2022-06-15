package com.store.drinks.repository.querys.dashBoard;

import com.store.drinks.entidade.ItensVenda;
import com.store.drinks.entidade.Produto;
import com.store.drinks.entidade.Venda;
import com.store.drinks.entidade.dto.produtosMaisVendidos.ProdutosMaisVendidosdto;
import com.store.drinks.repository.util.JpaUtils;
import com.store.drinks.repository.util.Multitenancy;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
import org.springframework.stereotype.Component;

@Component
public class ProdutoMaisVendidosImpl implements ProdutosMaisVendidosRepositoryCustom {
  
  @PersistenceContext
  private EntityManager manager;

  @Autowired
  private JpaUtils jpaUtils;

  @Autowired
  private Multitenancy multitenancy;

  @Override
  public List<ProdutosMaisVendidosdto> listProdutosMaisVendidos(ProdutosMaisVendidosFilters filters) {
    CriteriaBuilder builder = manager.getCriteriaBuilder();
    CriteriaQuery<Tuple> query = builder.createQuery(Tuple.class);
    Root<ItensVenda> root = query.from(ItensVenda.class);
    Join<ItensVenda, Produto> produto = root.join("produto");
    Join<ItensVenda, Venda> venda = root.join("venda");

    Predicate predicates;
    List<Selection<?>> selections = new ArrayList<>();

    selections.add(builder.sum(root.get("quantidade")).alias("quantidade"));
    selections.add(produto.get("descricaoProduto").alias("descricaoProduto"));
    query.multiselect(selections);
    
    if(Objects.nonNull(filters) && StringUtils.isNotBlank(filters.getDataInicial()) && StringUtils.isNotBlank(filters.getDataFinal())) {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault());
      LocalDate dataInicial = LocalDate.parse(filters.getDataInicial(), formatter);
      LocalDate dataFinal = LocalDate.parse(filters.getDataFinal(), formatter);
      predicates = builder.between(venda.get("dataHoraVenda"), dataInicial.atStartOfDay(), dataFinal.atStartOfDay().plusDays(1));
    } else {
      LocalDateTime dateInicio = LocalDateTime.now().withDayOfMonth(1);
      LocalDateTime dateFim = LocalDateTime.now().withMonth(dateInicio.getMonthValue()+1).withDayOfMonth(1);
      predicates = builder.between(venda.get("dataHoraVenda"), dateInicio, dateFim);
    }
    
    query.where(
      predicates,
      builder.and(builder.equal(root.get("tenant"),multitenancy.getTenantValue()))
    );
    
    query.groupBy(produto.get("descricaoProduto"));
    query.orderBy(builder.desc(builder.sum(root.get("quantidade"))));
    
    TypedQuery<Tuple> typedQuery = manager.createQuery(query);
    typedQuery.setMaxResults(25);
    
    List<Tuple> listTuple = typedQuery.getResultList();
    List<ProdutosMaisVendidosdto> list = jpaUtils.converterTupleInDataTransferObject(listTuple,ProdutosMaisVendidosdto.class);
    
    return list;
  }
}
