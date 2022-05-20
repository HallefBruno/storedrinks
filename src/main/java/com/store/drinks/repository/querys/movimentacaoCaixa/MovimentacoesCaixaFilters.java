

package com.store.drinks.repository.querys.movimentacaoCaixa;

import com.store.drinks.entidade.dto.UsuarioSelect2;
import java.io.Serializable;
import java.time.LocalDate;

public class MovimentacoesCaixaFilters implements Serializable {
  
  private UsuarioSelect2 usuarioSelect2;
  private LocalDate dataInicio;
  private LocalDate dataFim;
  private Boolean somenteCaixaAberto;

  public MovimentacoesCaixaFilters() {
  }

  public MovimentacoesCaixaFilters(UsuarioSelect2 usuarioSelect2, LocalDate dataInicio, LocalDate dataFim, Boolean somenteCaixaAberto) {
    this.usuarioSelect2 = usuarioSelect2;
    this.dataInicio = dataInicio;
    this.dataFim = dataFim;
    this.somenteCaixaAberto = somenteCaixaAberto;
  }

  public UsuarioSelect2 getUsuarioSelect2() {
    return usuarioSelect2;
  }

  public void setUsuarioSelect2(UsuarioSelect2 usuarioSelect2) {
    this.usuarioSelect2 = usuarioSelect2;
  }

  public LocalDate getDataInicio() {
    return dataInicio;
  }

  public void setDataInicio(LocalDate dataInicio) {
    this.dataInicio = dataInicio;
  }

  public LocalDate getDataFim() {
    return dataFim;
  }

  public void setDataFim(LocalDate dataFim) {
    this.dataFim = dataFim;
  }

  public Boolean getSomenteCaixaAberto() {
    return somenteCaixaAberto;
  }

  public void setSomenteCaixaAberto(Boolean somenteCaixaAberto) {
    this.somenteCaixaAberto = somenteCaixaAberto;
  }
  
  
  
}
