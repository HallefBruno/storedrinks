package com.store.drinks.service;

import com.store.drinks.entidade.Caixa;
import com.store.drinks.entidade.dto.caixa.Caixadto;
import com.store.drinks.entidade.dto.caixa.DetalheSangriadto;
import com.store.drinks.execption.CaixaAbertoPorUsuarioException;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.store.drinks.repository.CaixaRepository;
import com.store.drinks.repository.MovimentacaoCaixaRepository;
import com.store.drinks.repository.util.Multitenancy;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CaixaService {

  private final CaixaRepository caixaRepository;
  private final UsuarioService usuarioService;
  private final MovimentacaoCaixaRepository movimentacaoCaixaRepository;
  private final Multitenancy multitenancy;

  @Transactional
  public void salvar(Caixa caixa) {
    if (abrirCaixaPorUsuarioLogado()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O caixa já foi aberto para esse usuário!");
    }
    Caixa acx = new Caixa();
    acx.setAberto(Boolean.TRUE);
    acx.setDataHoraAbertura(LocalDateTime.now());
    acx.setUsuario(UsuarioService.usuarioLogado());
    acx.setValorInicialTroco(caixa.getValorInicialTroco());
    caixaRepository.save(acx);
  }
  
  @Transactional
  public void fecharCaixa(Long id) {
    if (Objects.nonNull(id)) {
      Caixa caixa = getCaixa(id);
      caixa.setAberto(Boolean.FALSE);
      caixa.setDataHoraFechamento(LocalDateTime.now());
      caixaRepository.save(caixa);
    }
  }

  public boolean abrirCaixaPorUsuarioLogado() {
    try {
      Optional<Caixadto> cxAberto = caixaAberto(null);
      return cxAberto.isPresent();
    } catch (IncorrectResultSizeDataAccessException ex) {
      throw new CaixaAbertoPorUsuarioException("Não é possível realizar a venda, pois existe mais de um caixa aberto para este usuário");
    } catch (ResponseStatusException rs) {
      if (Objects.nonNull(rs.getReason()) && rs.getReason().contains("Nenhum caixa aberto para esse usuário!")) {
        return false;
      }
    }
    return false;
  }

  public BigDecimal valorTotalEmVendasPorUsuario(Long id) {
    if (Objects.nonNull(id)) {
      return movimentacaoCaixaRepository.valorTotalEmVendasPorUsuario(caixaRepository.findByAbertoTrueAndUsuarioId(id).get().getId()).get();
    }
    return movimentacaoCaixaRepository.valorTotalEmVendasPorUsuario(caixaRepository.findByAbertoTrueAndUsuarioId(UsuarioService.usuarioLogado().getId()).get().getId()).get();
  }

  public Caixa getCaixa(Long id) {
    Caixa caixa = new Caixa();
    Caixadto abrirCaixadto = caixaAberto(id).get();
    caixa.setId(abrirCaixadto.getId());
    caixa.setAberto(abrirCaixadto.getAberto());
    caixa.setDataHoraAbertura(abrirCaixadto.getDataHoraAbertura());
    caixa.setTenant(abrirCaixadto.getTenant());
    caixa.setValorInicialTroco(abrirCaixadto.getValorInicialTroco());
    if (Objects.nonNull(id)) {
      caixa.setUsuario(usuarioService.findByClienteSistemaTenantAndId(multitenancy.getTenantValue(), id).get());
      return caixa;
    }
    caixa.setUsuario(UsuarioService.usuarioLogado());
    return caixa;
  }

  public Optional<Caixadto> caixaAberto(Long id) {
    Optional<Caixadto> optionalCaixa;
    if(Objects.nonNull(id)) {
      optionalCaixa = caixaRepository.findByAbertoTrueAndUsuarioId(id);
      return optionalCaixa.map(op -> {
        return Optional.of(op); 
      }).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nenhum caixa aberto para esse usuário!"));
    }
    optionalCaixa = caixaRepository.findByAbertoTrueAndUsuario(UsuarioService.usuarioLogado());
    return optionalCaixa.map(op -> {
      return Optional.of(op);
    }).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nenhum caixa aberto para esse usuário!"));
  }
  
  public List<DetalheSangriadto> getListdetalheSangria() {
    List<DetalheSangriadto> detalhesSangria = caixaRepository.getListdetalheSangria(null);
    return detalhesSangria;
  }
  
}
