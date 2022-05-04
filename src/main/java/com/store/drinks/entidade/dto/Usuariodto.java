
package com.store.drinks.entidade.dto;

import java.io.Serializable;
import java.math.BigInteger;

public class Usuariodto implements Serializable {
  
  private BigInteger id;
  private String text;
  private String nome;
  private String destinatario;
  private String tenant;

  public Usuariodto(BigInteger id, String text, String nome, String destinatario, String tenant) {
    this.id = id;
    this.text = text;
    this.nome = nome;
    this.destinatario = destinatario;
    this.tenant = tenant;
  }

  public Usuariodto() {
  }
  
  public BigInteger getId() {
    return id;
  }

  public void setId(BigInteger id) {
    this.id = id;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getDestinatario() {
    return destinatario;
  }

  public void setDestinatario(String destinatario) {
    this.destinatario = destinatario;
  }

  public String getTenant() {
    return tenant;
  }

  public void setTenant(String tenant) {
    this.tenant = tenant;
  }

}
