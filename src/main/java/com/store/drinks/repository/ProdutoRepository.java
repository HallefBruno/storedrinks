package com.store.drinks.repository;

import com.store.drinks.entidade.Produto;
import com.store.drinks.repository.querys.produto.ProdutoRepositoryCustom;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long>, ProdutoRepositoryCustom {
    Optional<Produto> findByCodigoBarraAndAtivoTrueAndTenant(String codigoBarra, String tenant);
}
