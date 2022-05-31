package com.store.drinks.repository;

import com.store.drinks.entidade.Produto;
import com.store.drinks.repository.querys.produto.ProdutoRepositoryCustom;
import java.util.Optional;
import javax.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long>, ProdutoRepositoryCustom {
  
  Optional<Produto> findByCodigoBarraAndAtivoTrueAndTenant(String codigoBarra, String tenant);
  
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("from Produto p where p.codigoBarra = ?1 and p.ativo = true and p.tenant = ?2 ")
  Optional<Produto> findByProdutoForUpdate(String codigoBarra, String tenant);
  
}
