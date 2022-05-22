

package com.store.drinks.repository.querys.movimentacaoCaixa;

import com.store.drinks.entidade.Caixa;
import com.store.drinks.entidade.ClienteSistema;
import com.store.drinks.entidade.FormaPagamento;
import com.store.drinks.entidade.MovimentacaoCaixa;
import com.store.drinks.entidade.Usuario;
import com.store.drinks.entidade.Venda;
import com.store.drinks.entidade.dto.movimentacaoCaixa.MovimentacaoCaixadto;
import com.store.drinks.entidade.dto.usuario.UsuarioMovimentacaoCaixadto;
import com.store.drinks.entidade.wrapper.DataTableWrapper;
import com.store.drinks.repository.util.JpaUtils;
import com.store.drinks.repository.util.Multitenancy;
import com.store.drinks.repository.util.RowsUtil;
import com.store.drinks.service.UsuarioService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import org.apache.commons.lang3.BooleanUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

public class MovimentacaoCaixaRepositoryImpl implements MovimentacaoCaixaRepositoryCustom {
  
  @PersistenceContext
  private EntityManager manager;
  
  @Autowired
  private UsuarioService usuarioService;
  
  @Autowired
  private Multitenancy multitenancy;
  
  @Autowired
  private JpaUtils jpaUtils;
  
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

//  @Override
//  public DataTableWrapper<MovimentacaoCaixa> movimentacoesCaixa(MovimentacoesCaixaFilters movimentacoesCaixaFilters, int draw, int start) {
//    DataTableWrapper<MovimentacaoCaixa> dataTableWrapper = new DataTableWrapper<>();
//    CriteriaBuilder builder = manager.getCriteriaBuilder();
//    CriteriaQuery<Tuple> query = builder.createQuery(Tuple.class);
//    Root<MovimentacaoCaixa> movimentacaoCaixa = query.from(MovimentacaoCaixa.class);
//    Join<MovimentacaoCaixa, Caixa> caixa = movimentacaoCaixa.join("caixa");
//    Join<MovimentacaoCaixa, Usuario> usuario = movimentacaoCaixa.join("usuario");
//    List<Selection<?>> selections = new ArrayList<>();
//    List<Predicate> predicates = new ArrayList<>();
//    
//    selections.add(builder.coalesce(builder.sum(movimentacaoCaixa.get("valorRecebido")),BigDecimal.ZERO));
//    selections.add(builder.coalesce(builder.sum(movimentacaoCaixa.get("valorTroco")),BigDecimal.ZERO));
//    selections.add(usuario.get("id"));
//    selections.add(caixa.get("aberto"));
//    
//    if(movimentacoesCaixaFilters.getSomenteCaixaAberto()) {
//      predicates.add(builder.isTrue(caixa.get("aberto")));
//    } else {
//      predicates.add(builder.isFalse(caixa.get("aberto")));
//    }
//    
//    query.multiselect(selections);
//    query.where(predicates.toArray(Predicate[]::new));
//    
//    TypedQuery<Tuple> typedQuery = manager.createQuery(query);
//    typedQuery.setFirstResult(start);
//    typedQuery.setMaxResults(10);
//    
//    CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
//    Root<MovimentacaoCaixa> tagCountRoot = countQuery.from(MovimentacaoCaixa.class);
//    countQuery.select(builder.count(tagCountRoot)).where(predicates.toArray(Predicate[]::new));
//    Long count = manager.createQuery(countQuery).getResultList().get(0);
//    
//    dataTableWrapper.setData(null);
//    dataTableWrapper.setRecordsTotal(count);
//    dataTableWrapper.setDraw(draw);
//    dataTableWrapper.setStart(start);
//    
//    return dataTableWrapper;
//  }
  
  @Override
  public List<UsuarioMovimentacaoCaixadto> usuariosMovimentacaoCaixa() {
    CriteriaBuilder builder = manager.getCriteriaBuilder();
    CriteriaQuery<Tuple> query = builder.createQuery(Tuple.class);
    Root<Usuario> root = query.from(Usuario.class);
    Join<Usuario, ClienteSistema> clienteSistema = root.join("clienteSistema");
    query.multiselect(root.get("id").alias("id"), root.get("nome").alias("text"));
    query.where(
      builder.equal(clienteSistema.get("tenant"), multitenancy.getTenantValue())
    );
    List<Tuple> listTuple = manager.createQuery(query).getResultList();
    List<UsuarioMovimentacaoCaixadto> usuariosMovimentacaoCaixa = jpaUtils.converterTupleInDataTransferObject(listTuple,UsuarioMovimentacaoCaixadto.class);
    return usuariosMovimentacaoCaixa;
  }
  
  @Override
  public DataTableWrapper<MovimentacaoCaixadto> movimentacoesCaixa(MovimentacoesCaixaFilters movimentacoesCaixaFilters, int draw, int start) {
    DataTableWrapper<MovimentacaoCaixadto> dataTableWrapper = new DataTableWrapper<>();
    
    CriteriaBuilder builder = manager.getCriteriaBuilder();
    CriteriaBuilder cbCount = manager.getCriteriaBuilder();
    CriteriaQuery<Tuple> query = builder.createQuery(Tuple.class);
    
    List<Selection<?>> selections = new ArrayList<>();
    
    Root<MovimentacaoCaixa> movimentacaoCaixa = query.from(MovimentacaoCaixa.class);
    Join<MovimentacaoCaixa, Caixa> caixa = movimentacaoCaixa.join("caixa");
    Join<MovimentacaoCaixa, Usuario> usuario = movimentacaoCaixa.join("usuario");
    Join<MovimentacaoCaixa, Venda> venda = movimentacaoCaixa.join("venda",JoinType.LEFT);
    
    List<Predicate> predicates = new ArrayList<>();
    
    selections.add(movimentacaoCaixa.get("id").alias("movimentacaoId"));
    selections.add(usuario.get("id").alias("usuarioId"));
    selections.add(venda.get("id").alias("vendaId"));
    selections.add(caixa.get("id").alias("caixaId"));
    selections.add(movimentacaoCaixa.get("valorRecebido").alias("valorRecebido"));
    
    query.multiselect(selections);
    
    if (movimentacoesCaixaFilters.getSomenteCaixaAberto()) {
      predicates.add(builder.isTrue(caixa.get("aberto")));
    } else {
      predicates.add(builder.isFalse(caixa.get("aberto")));
    }
    
    query.where(predicates.toArray(Predicate[]::new));
    List<Tuple> listTuple = manager.createQuery(query).getResultList();
    List<MovimentacaoCaixadto> usuariosMovimentacaoCaixa = jpaUtils.converterTupleInDataTransferObject(listTuple,MovimentacaoCaixadto.class);
    
    CriteriaQuery<Long> countQuery = cbCount.createQuery(Long.class);
    Root<MovimentacaoCaixa> tagCountRoot = countQuery.from(MovimentacaoCaixa.class);
    
    countQuery.select(cbCount.count(tagCountRoot));
    countQuery.where(specificationForCount(movimentacoesCaixaFilters, draw, start).toPredicate(tagCountRoot, countQuery, cbCount));
    Long count = manager.createQuery(countQuery).getResultList().get(0);
    
    dataTableWrapper.setData(usuariosMovimentacaoCaixa);
    dataTableWrapper.setRecordsTotal(count);
    dataTableWrapper.setDraw(draw);
    dataTableWrapper.setStart(start);
    
    return null;
  }

  private Specification<MovimentacaoCaixa> specificationForCount(MovimentacoesCaixaFilters movimentacoesCaixaFilters, int draw, int start) {
    return (Root<MovimentacaoCaixa> movimentacaoCaixa, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();
      Join<MovimentacaoCaixa, Caixa> caixa = movimentacaoCaixa.join("caixa");
      Join<MovimentacaoCaixa, Usuario> usuario = movimentacaoCaixa.join("usuario");
      Join<MovimentacaoCaixa, Venda> venda = movimentacaoCaixa.join("venda");
      
      if (movimentacoesCaixaFilters.getSomenteCaixaAberto()) {
        predicates.add(criteriaBuilder.isTrue(caixa.get("aberto")));
      } else {
        predicates.add(criteriaBuilder.isFalse(caixa.get("aberto")));
      }
      return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
    };
  }
  
}