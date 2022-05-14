

package com.store.drinks.repository.querys.movimentacaoCaixa;

import com.store.drinks.entidade.Caixa;
import com.store.drinks.entidade.MovimentacaoCaixa;
import com.store.drinks.entidade.Usuario;
import com.store.drinks.entidade.wrapper.DataTableWrapper;
import com.store.drinks.repository.util.Multitenancy;
import com.store.drinks.repository.util.RowsUtil;
import com.store.drinks.service.UsuarioService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

public class MovimentacaoCaixaRepositoryImpl implements MovimentacaoCaixaRepositoryCustom {
  
  @PersistenceContext
  private EntityManager manager;
  
  @Autowired
  private UsuarioService usuarioService;
  
  @Autowired
  private Multitenancy multitenancy;
  
  @Autowired
  private RowsUtil<MovimentacaoCaixa> rowsUtil;
  
  private static final String ADMIN = "Administrador";
  private static final String SUPER = "SuperUser";
  
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

  @Override
  public DataTableWrapper<MovimentacaoCaixa> movimentacoesCaixa(MovimentacoesCaixaFilters movimentacoesCaixaFilters, int draw, int start) {
    DataTableWrapper<MovimentacaoCaixa> dataTableWrapper = new DataTableWrapper<>();
    CriteriaBuilder builder = manager.getCriteriaBuilder();
    CriteriaQuery<MovimentacaoCaixa> query = builder.createQuery(MovimentacaoCaixa.class);
    Root<MovimentacaoCaixa> movimentacaoCaixa = query.from(MovimentacaoCaixa.class);
    Join<MovimentacaoCaixa, Caixa> caixa = movimentacaoCaixa.join("caixa");
    Join<MovimentacaoCaixa, Usuario> usuario = movimentacaoCaixa.join("usuario");
    //Join<MovimentacaoCaixa, FormaPagamento> formaPagamento = movimentacaoCaixa.join("formaPagamento",JoinType.LEFT);
    List<Predicate> predicates = new ArrayList<>();
    var ususarioLogado = usuarioService.usuarioLogado();
    var permissaoAdmin = ususarioLogado.getGrupos().stream().filter(grupo -> grupo.getNome().equals(ADMIN) || grupo.getNome().equals(SUPER)).findFirst();
    
    if(permissaoAdmin.isPresent()) {
      predicates.add(builder.equal(movimentacaoCaixa.get("tenant"),multitenancy.getTenantValue()));
      //if(Objects.nonNull(movimentacoesCaixaFilters.getUsuarioSelect2().getUsuarioId())) {
        predicates.add(builder.and(builder.equal(usuario.get("id"), ususarioLogado.getId())));
      //}
    }
    
    if(BooleanUtils.isTrue(movimentacoesCaixaFilters.getSomenteCaixaAberto())) {
      predicates.add(builder.isTrue(caixa.get("aberto")));
    } else {
      predicates.add(builder.isFalse(caixa.get("aberto")));
    }
    
    query.select(movimentacaoCaixa);
    query.where(predicates.toArray(Predicate[]::new));
    
    TypedQuery<MovimentacaoCaixa> typedQuery = manager.createQuery(query);
    typedQuery.setFirstResult(start);
    typedQuery.setMaxResults(10);
    Long count = rowsUtil.countRows(builder, query, movimentacaoCaixa, manager);
    
    dataTableWrapper.setData(typedQuery.getResultList());
    dataTableWrapper.setRecordsTotal(count);
    dataTableWrapper.setDraw(draw);
    dataTableWrapper.setStart(start);
    
    return dataTableWrapper;
  }
}
