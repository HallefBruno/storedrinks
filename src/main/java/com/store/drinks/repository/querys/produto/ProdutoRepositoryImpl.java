package com.store.drinks.repository.querys.produto;

import com.store.drinks.entidade.Produto;
import com.store.drinks.entidade.enuns.Tenant;
import com.store.drinks.entidade.ETenant;
import com.store.drinks.execption.NegocioException;
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
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class ProdutoRepositoryImpl extends ETenant implements ProdutoRepositoryCustom {

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
        
        predicate = cb.and(cb.equal(cb.upper(morador.get(Tenant.nome.value())), getTenantValue().toUpperCase()));
        predicates.add(predicate);
        
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
        
        Predicate prediTenant = cb.and(cb.equal(cb.upper(produto.get(Tenant.nome.value())), getTenantValue().toUpperCase()));
        
        query.select(produto);
        query.where(predicateOr, cb.isTrue(isAtivo), prediTenant);
        TypedQuery<Produto> typedQuery = manager.createQuery(query);
        typedQuery.setFirstResult(primeiroRegistro);
        typedQuery.setMaxResults(totalRegistrosPorPagina);
        Long count = rowsUtil.countRows(cb, query, produto, manager);
        return new PageImpl<>(typedQuery.getResultList(),pageable, count);

    }

    @Override
    public void verificarExistenciaProduto(Produto produto) {
        Query query = manager.createNamedQuery("find produto");
        query.setParameter(1, produto.getDescricaoProduto());
        query.setParameter(2, produto.getCodigoBarra());
        query.setParameter(3, produto.getCodProduto());
        query.setParameter(4, produto.getTenantValue());
        if(!query.setMaxResults(1).getResultList().isEmpty()) {
            String msg = String.format("Encontra-se no sistema caracteristica desse produto: %s, %s, %s", produto.getDescricaoProduto(),  produto.getCodigoBarra(), produto.getCodProduto());
            throw new NegocioException(msg);
        }
    }
    
    
}
