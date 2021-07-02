package com.store.drinks.repository;

import com.store.drinks.entidade.Produto;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    public Optional<Produto> findByCodigoBarraOrCodProduto(String codBarra, String codProduto);
}
