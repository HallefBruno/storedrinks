package com.store.drinks.repository.querys.produto;

import com.store.drinks.entidade.Produto;
import com.store.drinks.repository.util.RowsUtil;
import java.util.ArrayList;
import java.util.List;
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

public class ProdutoRepositoryImpl implements ProdutoRepositoryCustom {

    @PersistenceContext
    private EntityManager manager;
    
    @Autowired
    private RowsUtil rowsUtil;
    
    @Override
    public Page<Produto> filtrar(ProdutoFilter filtro, Pageable pageable) {
        
        int paginaAtual = pageable.getPageNumber();
        int totalRegistrosPorPagina = pageable.getPageSize();
        int primeiroRegistro = paginaAtual * totalRegistrosPorPagina;
        
        CriteriaBuilder cb = manager.getCriteriaBuilder();
        CriteriaQuery<Produto> query = cb.createQuery(Produto.class);
        Root<Produto> morador = query.from(Produto.class);
        List<Predicate> predicates = new ArrayList<>();
        Predicate predicate;
        
        if (!StringUtils.isBlank(filtro.getCodBarra())) {
            predicate = cb.like(cb.upper(morador.get("codigoBarra")), "%" + filtro.getCodBarra().toUpperCase() + "%");
            predicates.add(predicate);
        }
        
        if (!StringUtils.isBlank(filtro.getCodProduto())) {
            predicate = cb.like(cb.upper(morador.get("codProduto")), "%" + filtro.getCodProduto().toUpperCase() + "%");
            predicates.add(predicate);
        }
        
        if (!StringUtils.isBlank(filtro.getDescricao())) {
            predicate = cb.like(cb.upper(morador.get("descricaoProduto")), "%" + filtro.getDescricao().toUpperCase() + "%");
            predicates.add(predicate);
        }
        
        query.select(morador);
        query.where(predicates.toArray(new Predicate[]{}));
        TypedQuery<Produto> typedQuery = manager.createQuery(query);
        typedQuery.setFirstResult(primeiroRegistro);
        typedQuery.setMaxResults(totalRegistrosPorPagina);
        Long count = rowsUtil.countRows(cb, query, morador, manager);
        
        return new PageImpl<>(typedQuery.getResultList(),pageable, count);
    }
    
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
        
        query.select(produto);
        query.where(predicateOr, cb.isTrue(isAtivo));
        TypedQuery<Produto> typedQuery = manager.createQuery(query);
        typedQuery.setFirstResult(primeiroRegistro);
        typedQuery.setMaxResults(totalRegistrosPorPagina);
        Long count = rowsUtil.countRows(cb, query, produto, manager);
        return new PageImpl<>(typedQuery.getResultList(),pageable, count);

    }
}
