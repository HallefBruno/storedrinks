package com.store.drinks.repository;

import com.store.drinks.entidade.Mensagem;
import com.store.drinks.repository.querys.mensagem.MensagemRepositoryCustom;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MensagemRepository extends PagingAndSortingRepository<Mensagem, Long>, MensagemRepositoryCustom {
  List<Mensagem> findAllByLida(Boolean lida, Pageable pageable);
  
}
