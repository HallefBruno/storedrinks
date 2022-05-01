package com.store.drinks.repository;

import com.store.drinks.entidade.Caixa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.store.drinks.repository.querys.caixa.CaixaRepositoryCustom;

@Repository
public interface CaixaRepository extends JpaRepository<Caixa, Long>, CaixaRepositoryCustom {

}
