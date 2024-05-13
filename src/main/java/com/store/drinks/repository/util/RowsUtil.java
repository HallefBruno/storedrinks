package com.store.drinks.repository.util;

import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class RowsUtil<T> {

  public <T> Long countRows(final CriteriaBuilder criteriaBuilder, final CriteriaQuery<T> criteriaQuery, Root<T> root, EntityManager entityManager) {
    CriteriaQuery<Long> query = createCountQuery(criteriaBuilder, criteriaQuery, root);
    return entityManager.createQuery(query).getSingleResult();
  }
  
  public Long paginacao(final CriteriaBuilder criteriaBuilder, final CriteriaQuery<T> criteriaQuery, Root<T> root, EntityManager entityManager, TypedQuery<T> typedQuery, Pageable pageable) {
    int paginaAtual = pageable.getPageNumber();
    int totalRegistrosPorPagina = pageable.getPageSize();
    int primeiroRegistro = paginaAtual * totalRegistrosPorPagina;
    typedQuery.setFirstResult(primeiroRegistro);
    typedQuery.setMaxResults(totalRegistrosPorPagina);
    CriteriaQuery<Long> query = createCountQuery(criteriaBuilder, criteriaQuery, root);
    return entityManager.createQuery(query).getSingleResult();
  }

  private <T> CriteriaQuery<Long> createCountQuery(final CriteriaBuilder cb, final CriteriaQuery<T> criteria, final Root<T> root) {
    final CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
    final Root<T> countRoot = countQuery.from(criteria.getResultType());
    doJoins(root.getJoins(), countRoot);
    doJoinsOnFetches(root.getFetches(), countRoot);
    countQuery.select(cb.count(countRoot));
    countQuery.where(criteria.getRestriction());
    countRoot.alias(root.getAlias());
    return countQuery.distinct(criteria.isDistinct());
  }

  @SuppressWarnings("unchecked")
  private void doJoinsOnFetches(Set<? extends Fetch<?, ?>> joins, Root<?> root) {
    doJoins((Set<? extends Join<?, ?>>) joins, root);
  }

  private void doJoins(Set<? extends Join<?, ?>> joins, Root<?> root) {
    joins.forEach((join) -> {
      Join<?, ?> joined = root.join(join.getAttribute().getName(), join.getJoinType());
      joined.alias(join.getAlias());
      doJoins(join.getJoins(), joined);
    });
  }

  private void doJoins(Set<? extends Join<?, ?>> joins, Join<?, ?> root) {
    joins.forEach((join) -> {
      Join<?, ?> joined = root.join(join.getAttribute().getName(), join.getJoinType());
      joined.alias(join.getAlias());
      doJoins(join.getJoins(), joined);
    });
  }

}
