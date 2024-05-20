package com.store.drinks.service;

import com.store.drinks.entidade.MercadoLivre;
import com.store.drinks.entidade.enuns.PlataformasEcommerce;
import com.store.drinks.repository.MercadoLivreRepository;
import javax.servlet.http.HttpServletRequest;
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
  
  //@PreAuthorize("hasRole('SUPER_USER')")
  @Transactional
  public void salvar(String code, HttpServletRequest request) {
    var usuario = UsuarioService.usuarioLogado();
    if(usuario.getProprietario()) {
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
}
