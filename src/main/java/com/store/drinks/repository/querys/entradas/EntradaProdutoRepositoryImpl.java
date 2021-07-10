
package com.store.drinks.repository.querys.entradas;

import com.store.drinks.entidade.Produto;
import com.store.drinks.repository.util.RowsUtil;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

public class EntradaProdutoRepositoryImpl implements EntradaProdutoRepositoryCustom {
    
    @PersistenceContext
    private EntityManager manager;
    
    @Autowired
    private RowsUtil rowsUtil;
    
    @Override
    public Page<Produto> filtrarProdutosSelect(String descricao, Pageable pageable) {
        
        int paginaAtual = pageable.getPageNumber();
        int totalRegistrosPorPagina = pageable.getPageSize();
        int primeiroRegistro = paginaAtual * totalRegistrosPorPagina;
        
        CriteriaBuilder cb = manager.getCriteriaBuilder();
        CriteriaQuery<Produto> query = cb.createQuery(Produto.class);
        Root<Produto> produto = query.from(Produto.class);
        Predicate predicateOr = cb.conjunction();
        
        if (!StringUtils.isBlank(descricao)) {
            Predicate predicateCodBarra = cb.like(cb.upper(produto.get("codigoBarra")), "%" + descricao + "%");
            Predicate predicateCodProduto = cb.like(cb.upper(produto.get("codProduto")), "%" + descricao.toUpperCase() + "%");
            Predicate descricaoProduto = cb.like(cb.upper(produto.get("descricaoProduto")), "%" + descricao + "%");
            predicateOr = cb.or(predicateCodBarra, predicateCodProduto, descricaoProduto);
        }
        
        query.select(produto);
        query.where(predicateOr);
        TypedQuery<Produto> typedQuery = manager.createQuery(query);
        typedQuery.setFirstResult(primeiroRegistro);
        typedQuery.setMaxResults(totalRegistrosPorPagina);
        Long count = rowsUtil.countRows(cb, query, produto, manager);
        return new PageImpl<>(typedQuery.getResultList(),pageable, count);

    }
    
}
