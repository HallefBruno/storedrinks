package com.store.drinks.service;

import com.store.drinks.entidade.AbrirCaixa;
import com.store.drinks.execption.NegocioException;
import com.store.drinks.repository.AbrirCaixaRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AbrirCaixaService {

  @Autowired
  private AbrirCaixaRepository abrirCaixaRepository;

  @Autowired
  private UsuarioService usuarioService;

  @Transactional
  public void salvar(AbrirCaixa abrirCaixa) {
    if (abrirCaixaPorUsuarioLogado()) {
      throw new NegocioException("O caixa já foi aberto para esse usuário!");
    }
    AbrirCaixa acx = new AbrirCaixa();
    acx.setAberto(Boolean.TRUE);
    acx.setDataHoraAbertura(LocalDateTime.now());
    acx.setUsuario(usuarioService.usuarioLogado());
    acx.setValorInicialTroco(abrirCaixa.getValorInicialTroco());
    abrirCaixaRepository.save(acx);
  }

  public boolean abrirCaixaPorUsuarioLogado() {
    Optional<AbrirCaixa> caixaAberto = abrirCaixaRepository.findByAbertoTrueAndUsuario(usuarioService.usuarioLogado());
    return caixaAberto.isPresent();
  }

}
