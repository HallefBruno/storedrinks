package com.store.drinks.repository.querys.fornecedor;

import com.store.drinks.entidade.Fornecedor;
import com.store.drinks.entidade.enuns.Tenant;
import com.store.drinks.execption.NegocioException;
import com.store.drinks.repository.util.Multitenancy;
import com.store.drinks.repository.util.RowsUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class FornecedorRepositoryImpl implements FornecedorRepositoryCustom {

  @PersistenceContext
  private EntityManager manager;

  @Autowired
  private Multitenancy multitenancy;

  @Autowired
  private RowsUtil rowsUtil;

  @Override
  public void verificarExistenciaFornecedor(Fornecedor fornecedor) {
    Query query = manager.createNamedQuery("find fornecedor");
    query.setParameter(1, StringUtils.getDigits(fornecedor.getCpfCnpj()));
    query.setParameter(2, multitenancy.getTenantValue());
    List<Fornecedor> resultado = query.getResultList();
    if (!resultado.isEmpty()) {
      if (resultado.size() == 1 && Objects.isNull(fornecedor.getId())) {
        String msg = String.format("Encontra-se no sistema caracteristica desse fornecedor: %s", fornecedor.getCpfCnpj());
        throw new NegocioException(msg);
      } else if (resultado.size() > 1) {
        String msg = String.format("Encontra-se no sistema caracteristica desse fornecedor: %s", fornecedor.getCpfCnpj());
        throw new NegocioException(msg);
      }
    }
  }

  @Override
  public Page<Fornecedor> filtrar(FornecedorFilter fornecedorFilter, Pageable pageable) {

    int paginaAtual = pageable.getPageNumber();
    int totalRegistrosPorPagina = pageable.getPageSize();
    int primeiroRegistro = paginaAtual * totalRegistrosPorPagina;

    CriteriaBuilder cb = manager.getCriteriaBuilder();
    CriteriaQuery<Fornecedor> query = cb.createQuery(Fornecedor.class);
    Root<Fornecedor> fornecedor = query.from(Fornecedor.class);
    List<Predicate> predicates = new ArrayList<>();
    Predicate predicate;

    if (!StringUtils.isBlank(fornecedorFilter.getNomeFornecedor())) {
      predicate = cb.like(cb.upper(fornecedor.get("nome")), "%" + fornecedorFilter.getNomeFornecedor().toUpperCase() + "%");
      predicates.add(predicate);
    }

    if (!StringUtils.isBlank(fornecedorFilter.getCpfCnpj())) {
      predicate = cb.like(cb.upper(fornecedor.get("cpfCnpj")), "%" + StringUtils.getDigits(fornecedorFilter.getCpfCnpj()) + "%");
      predicates.add(predicate);
    }

    if (!StringUtils.isBlank(fornecedorFilter.getTelefone())) {
      predicate = cb.like(cb.upper(fornecedor.get("telefone")), "%" + StringUtils.getDigits(fornecedorFilter.getTelefone()) + "%");
      predicates.add(predicate);
    }

    predicate = cb.and(cb.equal(cb.upper(fornecedor.get(Tenant.nome.value())), multitenancy.getTenantValue().toUpperCase()));
    predicates.add(predicate);

    query.select(fornecedor);
    query.where(predicates.toArray(new Predicate[]{}));
    TypedQuery<Fornecedor> typedQuery = manager.createQuery(query);
    typedQuery.setFirstResult(primeiroRegistro);
    typedQuery.setMaxResults(totalRegistrosPorPagina);
    Long count = rowsUtil.countRows(cb, query, fornecedor, manager);

    return new PageImpl<>(typedQuery.getResultList(), pageable, count);

  }
}
