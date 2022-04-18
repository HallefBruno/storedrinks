
package com.store.drinks.repository;

import com.store.drinks.entidade.MensagensRecebidas;
import com.store.drinks.repository.querys.mensagensRecebidas.MensagensRecebidasRepositoryCustom;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MensagensRecebidasRepository extends JpaRepository<MensagensRecebidas, Long>, MensagensRecebidasRepositoryCustom {
  
  @Transactional
  @Modifying
  @Query("update MensagensRecebidas men set men.notificado = true where men.notificado = false and men.remetenteDestinatarioMensagem.destinatario = :destinatario")
  int updateNotificarMensagem(@Param("destinatario") String destinatario);
  
  Optional<MensagensRecebidas> findByIdAndRemetenteDestinatarioMensagemDestinatario(Long id, String email);
  Page<MensagensRecebidas> findByLidaAndRemetenteDestinatarioMensagemDestinatario(Boolean lida, String destinatario, Pageable pageable);

}
