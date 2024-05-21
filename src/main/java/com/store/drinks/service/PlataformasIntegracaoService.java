package com.store.drinks.service;

import com.store.drinks.entidade.PlataformasEcommerce;
import com.store.drinks.repository.PlataformasIntegracaoRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class PlataformasIntegracaoService {
  
  @Autowired
  private PlataformasIntegracaoRepository plataformasIntegracaoRepository;
  
  @Transactional
  public void salvar(PlataformasEcommerce plataformasIntegracao) {
    plataformasIntegracaoRepository.save(plataformasIntegracao);
  }
  
  @Transactional
  public void criarPlataformas() {
    
    var tenant = UsuarioService.usuarioLogado().getClienteSistema().getTenant();
    
    var plaMeli = new PlataformasEcommerce();
    plaMeli.setAtivo(Boolean.TRUE);
    plaMeli.setDataCadastro(LocalDate.now());
    plaMeli.setDescricao("Mercado Livre");
    plaMeli.setHtmlClass("btn btn-warning text-primary");
    plaMeli.setHtmlIdButton("btnMercadoLivre");
    plaMeli.setHtmlStyle("--bs-btn-padding-y: .25rem; --bs-btn-padding-x: .5rem; --bs-btn-font-size: .75rem; background-color: #fae634");
    plaMeli.setImagem("mercado-livre-logo.png");
    plaMeli.setImplantada(Boolean.FALSE);
    plaMeli.setTenant(tenant);
    
    var plaMagalu = new PlataformasEcommerce();
    plaMagalu.setAtivo(Boolean.TRUE);
    plaMagalu.setDataCadastro(LocalDate.now());
    plaMagalu.setDescricao("Magalu");
    plaMagalu.setHtmlClass("btn btn-primary");
    plaMagalu.setHtmlIdButton("btnMagalu");
    plaMagalu.setHtmlStyle("--bs-btn-padding-y: .25rem; --bs-btn-padding-x: .5rem; --bs-btn-font-size: .75rem;");
    plaMagalu.setImagem("magalu-logo.png");
    plaMagalu.setImplantada(Boolean.FALSE);
    plaMagalu.setTenant(tenant);
    
    var plaShopee = new PlataformasEcommerce();
    plaShopee.setAtivo(Boolean.TRUE);
    plaShopee.setDataCadastro(LocalDate.now());
    plaShopee.setDescricao("Shopee");
    plaShopee.setHtmlClass("'btn btn-danger");
    plaShopee.setHtmlIdButton("btnShopee");
    plaShopee.setHtmlStyle("--bs-btn-padding-y: .25rem; --bs-btn-padding-x: .5rem; --bs-btn-font-size: .75rem;");
    plaShopee.setImagem("shopee-logo.png");
    plaShopee.setImplantada(Boolean.FALSE);
    plaShopee.setTenant(tenant);
    
    plataformasIntegracaoRepository.saveAll(List.of(plaMeli, plaMagalu, plaShopee));
    
  }
  
  public PlataformasEcommerce getByDescricao(String descricao) {
    return plataformasIntegracaoRepository.findByDescricaoIgnoreCase(descricao).get();
  }
  
  public List<PlataformasEcommerce> todasAtivas() {
    return plataformasIntegracaoRepository.findAll(Example.of(PlataformasEcommerce.of(Boolean.TRUE, UsuarioService.usuarioLogado().getClienteSistema().getTenant())), Sort.by(Sort.Direction.ASC, "id"));
  }
  
}
