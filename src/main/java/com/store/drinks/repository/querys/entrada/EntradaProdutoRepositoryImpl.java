
package com.store.drinks.repository.querys.entrada;

import com.store.drinks.entidade.Produto;
import com.store.drinks.entidade.enuns.Tenant;
import com.store.drinks.repository.util.Multitenancy;
import com.store.drinks.repository.util.RowsUtil;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
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
            Predicate descricaoProduto = cb.like(cb.upper(produto.get("codProduto")),"%" + descricao + "%");
            predicateOr = cb.or(predicateCodBarra, predicateCodProduto, descricaoProduto);
        }
        
        Predicate prediTenant = cb.and(cb.equal(cb.upper(produto.get(Tenant.nome.value())), multitenancy.getTenantValue().toUpperCase()));
        
        query.select(produto);
        query.where(predicateOr, cb.isTrue(isAtivo), prediTenant);
        TypedQuery<Produto> typedQuery = manager.createQuery(query);
        typedQuery.setFirstResult(primeiroRegistro);
        typedQuery.setMaxResults(totalRegistrosPorPagina);
        Long count = rowsUtil.countRows(cb, query, produto, manager);
        return new PageImpl<>(typedQuery.getResultList(),pageable, count);

    }
    
}
