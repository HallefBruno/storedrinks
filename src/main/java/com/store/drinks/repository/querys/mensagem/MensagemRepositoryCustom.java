
package com.store.drinks.repository.querys.mensagem;

import com.store.drinks.entidade.dto.Usuariodto;
import com.store.drinks.entidade.dto.mensagem.Mensagemdto;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MensagemRepositoryCustom {
  
  @Cacheable("comercio")
  Page<Usuariodto> pesquisarComercioAutoComplete(String comercio, Pageable pageable);
  Boolean existemMensagensNaoLidas(String tenant, Long usuarioId);
  Page<Mensagemdto> findAllByLida(Boolean lida,String remetente, Pageable pageable);
  
}
