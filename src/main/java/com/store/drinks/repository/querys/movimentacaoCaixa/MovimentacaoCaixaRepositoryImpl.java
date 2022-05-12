

package com.store.drinks.repository.querys.movimentacaoCaixa;

import com.store.drinks.entidade.Venda;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

public class MovimentacaoCaixaRepositoryImpl implements MovimentacaoCaixaRepositoryCustom {
  
  @PersistenceContext
  private EntityManager manager;
  
  @Override
  public Optional<BigDecimal> valorTotalEmVendasPorUsuario() {
    CriteriaBuilder builder = manager.getCriteriaBuilder();
    CriteriaQuery<Tuple> query = builder.createQuery(Tuple.class);
    Root<Venda> root = query.from(Venda.class);
    List<Selection<?>> selections = new ArrayList<>();
    selections.add(builder.coalesce(builder.sum(root.get("valorTotalVenda")),BigDecimal.ZERO));
    query.multiselect(selections);
    var op = Optional.ofNullable(manager.createQuery(query).getResultList());
    if(op.isPresent()) {
      return Optional.of(new BigDecimal(op.get().get(0).get(0).toString()));
    }
    return Optional.of(BigDecimal.ZERO);
  }
}
