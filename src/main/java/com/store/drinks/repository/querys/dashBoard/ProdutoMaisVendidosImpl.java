package com.store.drinks.repository.querys.dashBoard;

import com.store.drinks.entidade.ItensVenda;
import com.store.drinks.entidade.Produto;
import com.store.drinks.entidade.Venda;
import com.store.drinks.entidade.dto.produtosMaisVendidos.ProdutosMaisVendidosdto;
import com.store.drinks.repository.util.JpaUtils;
import com.store.drinks.repository.util.Multitenancy;
import com.store.drinks.repository.util.RowsUtil;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProdutoMaisVendidosImpl implements ProdutosMaisVendidosRepositoryCustom {
  
  @PersistenceContext
  private EntityManager manager;

  @Autowired
  private RowsUtil rowsUtil;
  
  @Autowired
  private JpaUtils jpaUtils;

  @Autowired
  private Multitenancy multitenancy;

  @Override
  public List<ProdutosMaisVendidosdto> listProdutosMaisVendidos(LocalDate dataInicial, LocalDate dataFinal) {
    CriteriaBuilder builder = manager.getCriteriaBuilder();
    CriteriaQuery<Tuple> criteria = builder.createQuery(Tuple.class);
    Root<ItensVenda> root = criteria.from(ItensVenda.class);
    Join<ItensVenda, Produto> produto = root.join("produto");
    Join<ItensVenda, Venda> venda = root.join("venda");
    List<Selection<?>> selections = new ArrayList<>();
    selections.add(builder.sum(root.get("quantidade")));
    selections.add(produto.get("descricaoProduto"));
    criteria.multiselect(selections);
    criteria.where(
      builder.between(venda.get("dataHoraVenda"), LocalDateTime.now(), LocalDateTime.now()),
      builder.and(builder.equal(root.get("tenant"),multitenancy.getTenantValue()))
    );
    criteria.groupBy(produto.get("descricaoProduto"));
    criteria.orderBy(builder.desc(builder.sum(root.get("quantidade"))));
    
    List<Tuple> listTuple = manager.createQuery(criteria).getResultList();
    List<ProdutosMaisVendidosdto> list = jpaUtils.converterTupleInDataTransferObject(listTuple,ProdutosMaisVendidosdto.class);
    return list;
  }
}
