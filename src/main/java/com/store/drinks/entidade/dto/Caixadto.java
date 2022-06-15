package com.store.drinks.entidade.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Caixadto implements Serializable {

  private Long id;
  private Boolean aberto;
  private LocalDateTime dataHoraAbertura;
  private BigDecimal valorInicialTroco;
  private LocalDateTime dataHoraFechamento;
  private String tenant;
  private Long usuarioId;

  public Caixadto(Long id, Boolean aberto, LocalDateTime dataHoraAbertura, BigDecimal valorInicialTroco, LocalDateTime dataHoraFechamento, String tenant, Long usuarioId) {
    this.id = id;
    this.aberto = aberto;
    this.dataHoraAbertura = dataHoraAbertura;
    this.valorInicialTroco = valorInicialTroco;
    this.dataHoraFechamento = dataHoraFechamento;
    this.tenant = tenant;
    this.usuarioId = usuarioId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Boolean getAberto() {
    return aberto;
  }

  public void setAberto(Boolean aberto) {
    this.aberto = aberto;
  }

  public LocalDateTime getDataHoraAbertura() {
    return dataHoraAbertura;
  }

  public void setDataHoraAbertura(LocalDateTime dataHoraAbertura) {
    this.dataHoraAbertura = dataHoraAbertura;
  }

  public BigDecimal getValorInicialTroco() {
    return valorInicialTroco;
  }

  public void setValorInicialTroco(BigDecimal valorInicialTroco) {
    this.valorInicialTroco = valorInicialTroco;
  }

  public LocalDateTime getDataHoraFechamento() {
    return dataHoraFechamento;
  }

  public void setDataHoraFechamento(LocalDateTime dataHoraFechamento) {
    this.dataHoraFechamento = dataHoraFechamento;
  }

  public String getTenant() {
    return tenant;
  }

  public void setTenant(String tenant) {
    this.tenant = tenant;
  }

  public Long getUsuarioId() {
    return usuarioId;
  }

  public void setUsuarioId(Long usuarioId) {
    this.usuarioId = usuarioId;
  }
  
  
}
