package com.store.drinks.repository.querys.dashBoard;

import com.store.drinks.repository.filtros.ProdutosMaisVendidosFiltro;
import com.store.drinks.entidade.ItensVenda;
import com.store.drinks.entidade.Produto;
import com.store.drinks.entidade.Usuario;
import com.store.drinks.entidade.Venda;
import com.store.drinks.entidade.dto.produtosMaisVendidos.ProdutosMaisVendidosdto;
import com.store.drinks.repository.util.JpaUtils;
import com.store.drinks.service.UsuarioService;
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
  private EntityManager entityManager;

  @Autowired
  private JpaUtils jpaUtils;

  @Override
  public List<ProdutosMaisVendidosdto> listProdutosMaisVendidos(ProdutosMaisVendidosFiltro filters) {
    Usuario usuarioLogado = UsuarioService.usuarioLogado();
    
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Tuple> query = builder.createQuery(Tuple.class);
    Root<ItensVenda> root = query.from(ItensVenda.class);
    Join<ItensVenda, Produto> produto = root.join("produto");
    Join<ItensVenda, Venda> venda = root.join("venda");
    Join<Venda, Usuario> usuario = venda.join("usuario");

    Predicate predicate;
    List<Selection<?>> selections = new ArrayList<>();

    selections.add(builder.sum(root.get("quantidade")).alias("quantidade"));
    selections.add(produto.get("descricaoProduto").alias("descricaoProduto"));
    query.multiselect(selections);
    
    if(Objects.nonNull(filters) && StringUtils.isNotBlank(filters.getDataInicial()) && StringUtils.isNotBlank(filters.getDataFinal())) {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault());
      LocalDate dataInicial = LocalDate.parse(filters.getDataInicial(), formatter);
      LocalDate dataFinal = LocalDate.parse(filters.getDataFinal(), formatter);
      predicate = builder.between(venda.get("dataVenda"), dataInicial, dataFinal);
    } else {
      predicate = builder.greaterThanOrEqualTo(venda.get("dataVenda"), LocalDate.now());
    }
    
    query.where(
predicate,
builder.and(builder.equal(usuario.get("id"),usuarioLogado.getId())),
builder.and(builder.equal(root.get("tenant"),usuarioLogado.getClienteSistema().getTenant()))
    );
    
    query.groupBy(produto.get("descricaoProduto"));
    query.orderBy(builder.desc(builder.sum(root.get("quantidade"))));
    
    TypedQuery<Tuple> typedQuery = entityManager.createQuery(query);
    typedQuery.setMaxResults(50);
    
    List<Tuple> listTuple = typedQuery.getResultList();
    List<ProdutosMaisVendidosdto> list = jpaUtils.parseTuple(listTuple,ProdutosMaisVendidosdto.class);
    
    return list;
  }
}
