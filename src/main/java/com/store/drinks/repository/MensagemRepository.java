package com.store.drinks.repository;

import com.store.drinks.entidade.Mensagem;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MensagemRepository extends PagingAndSortingRepository<Mensagem, Long> {

  List<Mensagem> findAllByLida(Boolean lida, Pageable pageable);
  
}
