package com.store.drinks.repository;

import com.store.drinks.entidade.Mensagem;
import com.store.drinks.repository.querys.mensagem.MensagemRepositoryCustom;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MensagemRepository extends PagingAndSortingRepository<Mensagem, Long>, MensagemRepositoryCustom {
  List<Mensagem> findAllByLida(Boolean lida, Pageable pageable);
  
  @Transactional
  @Modifying
  @Query("update Mensagem men set men.notificado = true where men.notificado = false and men.tenant = :tenant and men.usuario.id = :id")
  int updateNotificarMensagem(@Param("tenant") String tenant, @Param("id") Long id);

}
