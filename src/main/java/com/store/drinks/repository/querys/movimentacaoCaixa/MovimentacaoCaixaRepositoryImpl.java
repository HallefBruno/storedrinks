

package com.store.drinks.repository.querys.movimentacaoCaixa;

import com.store.drinks.entidade.MovimentacaoCaixa;
import com.store.drinks.entidade.Usuario;
import com.store.drinks.repository.util.Multitenancy;
import com.store.drinks.service.UsuarioService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

public class MovimentacaoCaixaRepositoryImpl implements MovimentacaoCaixaRepositoryCustom {
  
  @PersistenceContext
  private EntityManager manager;
  
  @Autowired
  private UsuarioService usuarioService;
  
  @Autowired
  private Multitenancy multitenancy;
  
  @Override
  public Optional<BigDecimal> valorTotalEmVendasPorUsuario() {
    CriteriaBuilder builder = manager.getCriteriaBuilder();
    CriteriaQuery<Tuple> query = builder.createQuery(Tuple.class);
    Root<MovimentacaoCaixa> root = query.from(MovimentacaoCaixa.class);
    Join<MovimentacaoCaixa, Usuario> usuario = root.join("usuario");
    List<Selection<?>> selections = new ArrayList<>();
    selections.add(builder.coalesce(builder.sum(root.get("valorRecebido")),BigDecimal.ZERO));
    selections.add(builder.coalesce(builder.sum(root.get("valorTroco")),BigDecimal.ZERO));
    selections.add(usuario.get("id"));
    
    query.multiselect(selections);
    
    query.where(
      builder.equal(root.get("tenant"), multitenancy.getTenantValue()),
      builder.and(builder.equal(usuario.get("id"), usuarioService.usuarioLogado().getId()))
    );
    
    query.groupBy(usuario.get("id"));
    
    var op = Optional.ofNullable(manager.createQuery(query).getResultList());
    if(!ObjectUtils.isEmpty(op.get())) {
      BigDecimal valorRecebido = new BigDecimal(op.get().get(0).get(0).toString());
      BigDecimal valorTroco = new BigDecimal(op.get().get(0).get(1).toString());
      return Optional.of(valorRecebido.subtract(valorTroco));
    }
    return Optional.of(BigDecimal.ZERO);
  }
}
