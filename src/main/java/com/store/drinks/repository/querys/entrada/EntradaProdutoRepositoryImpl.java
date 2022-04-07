package com.store.drinks.repository.querys.entrada;

import com.store.drinks.entidade.EntradaProduto;
import com.store.drinks.entidade.Fornecedor;
import com.store.drinks.entidade.Produto;
import com.store.drinks.entidade.enuns.Tenant;
import com.store.drinks.repository.util.Multitenancy;
import com.store.drinks.repository.util.RowsUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class EntradaProdutoRepositoryImpl implements EntradaProdutoRepositoryCustom {

  @PersistenceContext
  private EntityManager manager;

  @Autowired
  private RowsUtil rowsUtil;

  @Autowired
  private Multitenancy multitenancy;

  @Override
  public Page<Produto> filtrarProdutosSelect(String descricao, Pageable pageable) {

    int paginaAtual = pageable.getPageNumber();
    int totalRegistrosPorPagina = pageable.getPageSize();
    int primeiroRegistro = paginaAtual * totalRegistrosPorPagina;

    CriteriaBuilder cb = manager.getCriteriaBuilder();
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
    query.where(predicateOr, cb.isTrue(isAtivo), prediTenant);
    TypedQuery<Produto> typedQuery = manager.createQuery(query);
    typedQuery.setFirstResult(primeiroRegistro);
    typedQuery.setMaxResults(totalRegistrosPorPagina);
    Long count = rowsUtil.countRows(cb, query, produto, manager);
    return new PageImpl<>(typedQuery.getResultList(), pageable, count);

  }

  @Override
  public Page<EntradaProduto> filtrar(EntradasFilter filtro, Pageable pageable) {

    int paginaAtual = pageable.getPageNumber();
    int totalRegistrosPorPagina = pageable.getPageSize();
    int primeiroRegistro = paginaAtual * totalRegistrosPorPagina;

    CriteriaBuilder cb = manager.getCriteriaBuilder();
    CriteriaQuery<EntradaProduto> query = cb.createQuery(EntradaProduto.class);
    Root<EntradaProduto> entrada = query.from(EntradaProduto.class);
    Join<EntradaProduto, Fornecedor> fornecedor = (Join) entrada.fetch("fornecedor");
    Join<EntradaProduto, Produto> produto = (Join) entrada.fetch("produto");
    List<Predicate> predicates = new ArrayList<>();
    Predicate predicate;

    if (!StringUtils.isBlank(filtro.getCodBarra())) {
      predicate = cb.like(cb.upper(produto.get("codigoBarra")), "%" + filtro.getCodBarra() + "%");
      predicates.add(predicate);
    }

    if (!StringUtils.isBlank(filtro.getFornecedor())) {
      predicate = cb.like(cb.upper(fornecedor.get("nome")), "%" + filtro.getFornecedor().toUpperCase() + "%");
      predicates.add(predicate);
    }

    if (!StringUtils.isBlank(filtro.getNumeroNota())) {
      predicate = cb.like(cb.upper(entrada.get("numeroNota")), "%" + filtro.getNumeroNota() + "%");
      predicates.add(predicate);
    }

    if (Objects.nonNull(filtro.getDataEmissao())) {
      Predicate pdDataInicio = cb.equal(entrada.get("dataEmissao"), filtro.getDataEmissao());
      predicates.add(pdDataInicio);
    }

    predicate = cb.and(cb.equal(cb.upper(entrada.get(Tenant.NAME.value())), multitenancy.getTenantValue().toUpperCase()));
    predicates.add(predicate);

    query.select(entrada);
    query.where(predicates.toArray(Predicate[]::new));
    TypedQuery<EntradaProduto> typedQuery = manager.createQuery(query);
    typedQuery.setFirstResult(primeiroRegistro);
    typedQuery.setMaxResults(totalRegistrosPorPagina);
    Long count = rowsUtil.countRows(cb, query, entrada, manager);
    return new PageImpl<>(typedQuery.getResultList(), pageable, count);
  }
}
