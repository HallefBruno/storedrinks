package com.store.drinks.repository.querys.mensagensRecebidas;

import com.store.drinks.entidade.dto.usuario.UsuarioMensagemdto;
import com.store.drinks.entidade.dto.mensagem.Mensagemdto;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MensagemRecebidaRepositoryCustom {
  
  @Cacheable("comercio")
  Page<UsuarioMensagemdto> pesquisarComercioAutoComplete(String comercio, Pageable pageable);
  Boolean existeMensagemNaoLida(String destinatario);
  Page<Mensagemdto> findAllByLida(Boolean lida,String remetente, Pageable pageable);
  
}
