package com.store.drinks.repository.querys.mensagensRecebidas;

import com.store.drinks.entidade.dto.Usuariodto;
import com.store.drinks.entidade.dto.mensagem.Mensagemdto;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MensagensRecebidasRepositoryCustom {
  
  @Cacheable("comercio")
  Page<Usuariodto> pesquisarComercioAutoComplete(String comercio, Pageable pageable);
  Boolean existemMensagensNaoLidas(String destinatario);
  Page<Mensagemdto> findAllByLida(Boolean lida,String remetente, Pageable pageable);
  
}
