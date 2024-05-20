package com.store.drinks.service;

import com.store.drinks.entidade.MercadoLivre;
import com.store.drinks.entidade.Usuario;
import com.store.drinks.entidade.enuns.PlataformasEcommerce;
import com.store.drinks.repository.MercadoLivreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MercadoLivreService {
  
  private MercadoLivreRepository mercadoLivreRepository;
  private PlataformasIntegracaoService plataformasIntegracaoService;
  private UsuarioService usuarioService;

  @Autowired
  public MercadoLivreService(MercadoLivreRepository mercadoLivreRepository, PlataformasIntegracaoService plataformasIntegracaoService, UsuarioService usuarioService) {
    this.mercadoLivreRepository = mercadoLivreRepository;
    this.plataformasIntegracaoService = plataformasIntegracaoService;
    this.usuarioService = usuarioService;
  }
  
  @Transactional
  public void salvar(String code) {
    var usuario = UsuarioService.usuarioLogado();
    if(usuario.getProprietario() && plataformaImplementada(usuario) == Boolean.FALSE) {
      var mercadoLivre = new MercadoLivre();
      mercadoLivre.setCode(code);
      mercadoLivre.setUsuario(usuario);
      var plataformaIntegracao = plataformasIntegracaoService.getByDescricao(PlataformasEcommerce.MERCADO_LIVRE.descricao());
      plataformaIntegracao.setImplantada(Boolean.TRUE);
      plataformasIntegracaoService.update(plataformaIntegracao);
      mercadoLivre.setPlataformasEcommerce(plataformaIntegracao);
      mercadoLivreRepository.save(mercadoLivre);
      usuarioService.updateUsuarioMercadoLivre(mercadoLivre);
    }
  }
  
  private boolean plataformaImplementada(Usuario usuario) {
    if (usuario.getMercadoLivre() != null) {
      return usuario.getMercadoLivre().getPlataformasEcommerce().getImplantada();
    }
    return false;
  }
}
