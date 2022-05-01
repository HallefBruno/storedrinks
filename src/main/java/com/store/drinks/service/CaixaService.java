package com.store.drinks.service;

import com.store.drinks.entidade.Caixa;
import com.store.drinks.entidade.dto.Caixadto;
import com.store.drinks.execption.CaixaAbertoPorUsuarioException;
import com.store.drinks.execption.NegocioException;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.store.drinks.repository.CaixaRepository;

@Service
@RequiredArgsConstructor
public class CaixaService {

  private final CaixaRepository abrirCaixaRepository;
  private final UsuarioService usuarioService;

  @Transactional
  public void salvar(Caixa caixa) {
    if (abrirCaixaPorUsuarioLogado()) {
      throw new NegocioException("O caixa já foi aberto para esse usuário!");
    }
    Caixa acx = new Caixa();
    acx.setAberto(Boolean.TRUE);
    acx.setDataHoraAbertura(LocalDateTime.now());
    acx.setUsuario(usuarioService.usuarioLogado());
    acx.setValorInicialTroco(caixa.getValorInicialTroco());
    abrirCaixaRepository.save(acx);
  }

  public boolean abrirCaixaPorUsuarioLogado() {
    try {
      Optional<Caixadto> cxAberto = caixaAberto();
      return cxAberto.isPresent();
    } catch (IncorrectResultSizeDataAccessException ex) {
      throw new CaixaAbertoPorUsuarioException("Não é possível realizar a venda, pois existe mais de um caixa aberto para este usuário");
    }
  }
  
  public Optional<Caixadto> caixaAberto() {
    return abrirCaixaRepository.findByAbertoTrueAndUsuario(usuarioService.usuarioLogado());
  }
  
  public Caixa getCaixa() {
    Caixa caixa = new Caixa();
    Caixadto abrirCaixadto = caixaAberto().get();
    caixa.setId(abrirCaixadto.getId());
    caixa.setAberto(abrirCaixadto.getAberto());
    caixa.setDataHoraAbertura(abrirCaixadto.getDataHoraAbertura());
    caixa.setTenant(abrirCaixadto.getTenant());
    caixa.setValorInicialTroco(abrirCaixadto.getValorInicialTroco());
    caixa.setUsuario(usuarioService.usuarioLogado());
    return caixa;
  }
}
