

package com.store.drinks.repository.querys.caixa;

import com.store.drinks.entidade.Usuario;
import com.store.drinks.entidade.dto.Caixadto;
import java.util.Optional;


public interface CaixaRepositoryCustom {
  
  Optional<Caixadto> findByAbertoTrueAndUsuario(Usuario usuario);
  Optional<Caixadto> findByAbertoTrueAndUsuarioId(Long id);
}
