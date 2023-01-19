package com.store.drinks.service;

import com.store.drinks.entidade.ClienteSistema;
import com.store.drinks.entidade.MensagensEnviadas;
import com.store.drinks.entidade.MensagensRecebidas;
import com.store.drinks.entidade.Usuario;
import com.store.drinks.entidade.ValidarCliente;
import com.store.drinks.entidade.embedded.RemetenteDestinatarioMensagem;
import com.store.drinks.execption.NegocioException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashSet;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Repository
@RequiredArgsConstructor
public class NovaContaClienteSistema {
  
  private final ValidarClienteService validarClienteService;
  private final ClienteSistemaService clienteSistemaService;
  private final GrupoService grupoService;
  private final UsuarioService usuarioService;
  private final MensagensEnviadasService mensagensEnviadasService;
  private final MensagensRecebidasService mensagensRecebidasService;

  @Transactional
  public void salvaValidarCliente(String cpfCnpj) {
    Optional<ValidarCliente> temCnpj = validarClienteService.findByCpfCnpj(cpfCnpj);
    if (temCnpj.isPresent() && temCnpj.get().getContaCriada()) {
      throw new NegocioException("Esse cliente já possui conta!");
    } else if (!temCnpj.isPresent()) {
      verificaSeClienteEstaCadastrado(cpfCnpj);
    }
  }
  
  public Boolean verificarCpfCnpj(String cpfCnpj) {
    String cpfCnpjBase64 = decodeBase64(cpfCnpj);
    String cpfCnpjSemMascara = StringUtils.getDigits(cpfCnpjBase64);
    Optional<ValidarCliente> temCnpj = validarClienteService.findByCpfCnpj(cpfCnpjSemMascara);
    return temCnpj.isPresent() && !temCnpj.get().getContaCriada();
  }

  private void verificaSeClienteEstaCadastrado(String cpfCnpj) {
    Optional<ClienteSistema> opClienteSistema = clienteSistemaService.buscarPorCpfCnpj(cpfCnpj);
    if (opClienteSistema.isPresent()) {
      ClienteSistema clienteSistema = opClienteSistema.get();
      if (clienteSistema.getPrimeiroAcesso() && clienteSistema.getAcessarTelaCriarLogin()) {
        ValidarCliente validarCliente = new ValidarCliente();
        validarCliente.setCpfCnpj(cpfCnpj);
        validarCliente.setDataValidacao(LocalDateTime.now());
        validarClienteService.salvar(validarCliente);
      }
    } else {
      throw new NegocioException("Cliente sem permisão para criar conta!");
    }
  }
  
  @Transactional
  public void criarNovaContaCliente(String cpfcnpj, String nome, LocalDate dataNascimento, String email, String senha) {
    String cpfCnpjDecod = StringUtils.getDigits(decodeBase64(cpfcnpj));
    if (verificarCpfCnpj(cpfcnpj)) {
      Optional<ValidarCliente> oPvalidarCliente = validarClienteService.findByCpfCnpj(cpfCnpjDecod);
      ValidarCliente validarCliente = oPvalidarCliente.get();
      if(!validarCliente.getContaCriada()) {
        Optional<ClienteSistema> opClienteSistema = clienteSistemaService.buscarPorCpfCnpj(cpfCnpjDecod);
        Usuario usuario = new Usuario();
        usuario.setAtivo(Boolean.TRUE);
        usuario.setProprietario(Boolean.TRUE);
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setDataNascimento(dataNascimento);
        usuario.setSenha(decodeBase64(senha));
        usuario.setClienteSistema(opClienteSistema.get());
        usuario.setGrupos(new HashSet<>(grupoService.gruposParaUsuarioNovaConta()));
        validarCliente.setContaCriada(Boolean.TRUE);
        validarClienteService.salvar(validarCliente);
        usuario = usuarioService.salvar(usuario);
        salvarMensagemEnviadaRecebida(email, nome, usuario);
      } else {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não foi possível criar a conta, entre em contato com a o Administrador!");
      }
    }
  }
  
  @Transactional
  private void salvarMensagemEnviadaRecebida(String emailDistinatario, String nome, Usuario usuarioDestino) {
    String emailSistema = "sud@storedrinks.com";
    String mensagem = String.format("Olá seja bem vindo %s!, Como está iniciando agora, você não possui nenhuma movimentação no sistema e isso já pode ser feito. Dúvidas sugestões? Me envie uma mensagem pelo sistema ou zap :) ", nome);
    Usuario usuarioSistema = usuarioService.findByEmailAndAtivoTrue(emailSistema).get();
    
    MensagensEnviadas mensagensEnviadas = new MensagensEnviadas();
    RemetenteDestinatarioMensagem remetenteDestinatarioMensagem = new RemetenteDestinatarioMensagem();
    remetenteDestinatarioMensagem.setDestinatario(emailDistinatario);
    remetenteDestinatarioMensagem.setMensagem(mensagem);
    remetenteDestinatarioMensagem.setRemetente(usuarioSistema.getEmail());
    mensagensEnviadas.setDataHoraMensagemEnviada(LocalDateTime.now());
    mensagensEnviadas.setRemetenteDestinatarioMensagem(remetenteDestinatarioMensagem);
    mensagensEnviadas.setUsuario(usuarioDestino);
    
    MensagensRecebidas mensagensRecebidas = new MensagensRecebidas();
    RemetenteDestinatarioMensagem destinatarioMensagem = new RemetenteDestinatarioMensagem();
    destinatarioMensagem.setDestinatario(emailDistinatario);
    destinatarioMensagem.setMensagem(mensagem);
    destinatarioMensagem.setRemetente(emailSistema);
    mensagensRecebidas.setRemetenteDestinatarioMensagem(destinatarioMensagem);
    mensagensRecebidas.setLida(Boolean.FALSE);
    mensagensRecebidas.setNotificado(Boolean.FALSE);
    mensagensRecebidas.setDataHoraMensagemRecebida(LocalDateTime.now());
    mensagensRecebidas.setUsuario(usuarioService.findByEmailAndAtivoTrue(emailSistema).get());
    
    mensagensEnviadasService.salvar(mensagensEnviadas);
    mensagensRecebidasService.salvar(mensagensRecebidas);
  }
  
  private String decodeBase64(String base64) {
    byte[] decodedBytes = Base64.getDecoder().decode(base64);
    String decodeString = new String(decodedBytes);
    return decodeString;
  }
}
