package com.store.drinks.repository.querys.produto;

import com.store.drinks.repository.filtros.ProdutoFiltro;
import com.store.drinks.entidade.Produto;
import com.store.drinks.entidade.Usuario;
import com.store.drinks.entidade.dto.ProdutoConferirEstoquedto;
import com.store.drinks.entidade.enuns.Tenant;
import com.store.drinks.repository.util.JpaUtils;
import com.store.drinks.repository.util.Multitenancy;
import com.store.drinks.repository.util.RowsUtil;
import com.store.drinks.service.UsuarioService;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ProdutoRepositoryImpl implements ProdutoRepositoryCustom {

  @PersistenceContext
  private EntityManager entityManager;

  @Autowired
  private RowsUtil rowsUtil;

  @Autowired
  private Multitenancy multitenancy;
  
  @Autowired
  private JpaUtils jpaUtils;
  
  @Autowired
  private UsuarioService usuarioService;

  @Override
  public Page<Produto> filtrar(ProdutoFiltro filtro, Pageable pageable) {

    int paginaAtual = pageable.getPageNumber();
    int totalRegistrosPorPagina = pageable.getPageSize();
    int primeiroRegistro = paginaAtual * totalRegistrosPorPagina;

    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Produto> query = cb.createQuery(Produto.class);
    Root<Produto> produto = query.from(Produto.class);
    List<Predicate> predicates = new ArrayList<>();
    Predicate predicate;

    if (!StringUtils.isBlank(filtro.getCodBarra())) {
      predicate = cb.like(cb.upper(produto.get("codigoBarra")), "%" + filtro.getCodBarra().toUpperCase() + "%");
      predicates.add(predicate);
    }

    if (!StringUtils.isBlank(filtro.getCodProduto())) {
      predicate = cb.like(cb.upper(produto.get("codProduto")), "%" + filtro.getCodProduto().toUpperCase() + "%");
      predicates.add(predicate);
    }

    if (!StringUtils.isBlank(filtro.getDescricao())) {
      predicate = cb.like(cb.upper(produto.get("descricaoProduto")), "%" + filtro.getDescricao().toUpperCase() + "%");
      predicates.add(predicate);
    }

    predicate = cb.and(cb.equal(cb.upper(produto.get(Tenant.NAME.value())), multitenancy.getTenantValue().toUpperCase()));
    predicates.add(predicate);

    query.select(produto);
    query.where(predicates.toArray(Predicate[]::new));
    TypedQuery<Produto> typedQuery = entityManager.createQuery(query);
    typedQuery.setFirstResult(primeiroRegistro);
    typedQuery.setMaxResults(totalRegistrosPorPagina);
    Long count = rowsUtil.countRows(cb, query, produto, entityManager);

    return new PageImpl<>(typedQuery.getResultList(), pageable, count);
  }

  @Override
  public Page<Produto> filtrarProdutosSelect(String descricao, Pageable pageable) {

    int paginaAtual = pageable.getPageNumber();
    int totalRegistrosPorPagina = pageable.getPageSize();
    int primeiroRegistro = paginaAtual * totalRegistrosPorPagina;

    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Produto> query = cb.createQuery(Produto.class);
    Root<Produto> produto = query.from(Produto.class);
    Path<Boolean> isAtivo = produto.get("ativo");
    Predicate predicateOr = cb.conjunction();

    if (!StringUtils.isBlank(descricao)) {
      Predicate predicateCodBarra = cb.like(cb.upper(produto.get("codigoBarra")), "%" + descricao + "%");
      Predicate predicateCodProduto = cb.like(cb.upper(produto.get("descricaoProduto")), "%" + descricao.toUpperCase() + "%");
      Predicate descricaoProduto = cb.like(cb.upper(produto.get("codProduto")), "%" + descricao + "%");
      predicateOr = cb.or(predicateCodBarra, predicateCodProduto, descricaoProduto);
    }

    Predicate prediTenant = cb.and(cb.equal(cb.upper(produto.get(Tenant.NAME.value())), multitenancy.getTenantValue().toUpperCase()));
    
    query.select(produto);
    query.where(predicateOr, cb.isTrue(isAtivo), prediTenant, cb.and(cb.gt(produto.get("quantidade"), 0)));
    TypedQuery<Produto> typedQuery = entityManager.createQuery(query);
    typedQuery.setFirstResult(primeiroRegistro);
    typedQuery.setMaxResults(totalRegistrosPorPagina);
    Long count = rowsUtil.countRows(cb, query, produto, entityManager);
    return new PageImpl<>(typedQuery.getResultList(), pageable, count);

  }

  @Override
  public void verificarExistenciaProduto(Produto produto) {
    Query query = entityManager.createNamedQuery("find produto");
    query.setParameter(1, produto.getDescricaoProduto());
    query.setParameter(2, produto.getCodigoBarra());
    query.setParameter(3, produto.getCodProduto());
    query.setParameter(4, multitenancy.getTenantValue());
    List<Produto> resultado = query.getResultList();
    if (!resultado.isEmpty()) {
      if (resultado.size() == 1 && Objects.isNull(produto.getId())) {
        String msg = String.format("Encontra-se no sistema caracteristica desse produto: %s, %s, %s", produto.getDescricaoProduto(), produto.getCodigoBarra(), produto.getCodProduto());
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, msg);
      } else if (resultado.size() > 1) {
        String msg = String.format("Encontra-se no sistema caracteristica desse produto: %s, %s, %s", produto.getDescricaoProduto(), produto.getCodigoBarra(), produto.getCodProduto());
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, msg);
      }
    }
  }

  @Override
  public List<ProdutoConferirEstoquedto> listProdutosConferirEstoque() {
    Usuario usuarioLogado = UsuarioService.usuarioLogado();
    
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Tuple> query = builder.createQuery(Tuple.class);
    Root<Produto> root = query.from(Produto.class);

    List<Predicate> predicates = new ArrayList<>();
    List<Selection<?>> selections = new ArrayList<>();

    selections.add(root.get("id").alias("id"));
    selections.add(root.get("ativo").alias("ativo"));
    selections.add(root.get("codProduto").alias("codProduto"));
    selections.add(root.get("codigoBarra").alias("codBarra"));
    selections.add(root.get("descricaoProduto").alias("descricaoProduto"));
    selections.add(root.get("quantidade").alias("quantidade"));
    
    query.multiselect(selections);

    predicates.add(builder.equal(root.get("tenant"), usuarioLogado.getClienteSistema().getTenant()));
    
    query.where(predicates.toArray(Predicate[]::new));
    query.orderBy(builder.desc(root.get("quantidade")));
    
    TypedQuery<Tuple> typedQuery = entityManager.createQuery(query);
    List<Tuple> listTuple = typedQuery.getResultList();
    List<ProdutoConferirEstoquedto> list = jpaUtils.parseTuple(listTuple, ProdutoConferirEstoquedto.class);
    
    //list.sort(Comparator.comparing(ProdutoConferirEstoquedto::getQuantidade, Comparator.reverseOrder()));
    
    //List<ProdutoConferirEstoquedto> order = new LinkedList<>(list);
    
    //dataTableWrapper.setData(order);
    
    return list;
  }
  
}
