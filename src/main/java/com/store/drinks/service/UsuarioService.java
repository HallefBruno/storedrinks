package com.store.drinks.service;

import com.store.drinks.entidade.Usuario;
import com.store.drinks.entidade.dto.EmailValido;
import com.store.drinks.entidade.dto.usuario.UsuarioMensagemdto;
import com.store.drinks.repository.UsuarioRepository;
import com.store.drinks.repository.filtros.UsuarioFiltro;
import com.store.drinks.security.UsuarioSistema;
import com.store.drinks.storage.StorageCloudnary;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioService {

  private final UsuarioRepository usuarioRepository;
  private final StorageCloudnary storageCloudnary;
  private final RestTemplate restTemplate = new RestTemplateBuilder().build();
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public Usuario salvar(Usuario usuario) {
    usuario.setClienteSistema(usuarioLogado().getClienteSistema());
    return usuarioRepository.save(usuario);
  }
  
  @Transactional
  public void salvar(Usuario usuario, MultipartFile image) {
    String fileName = StringUtils.cleanPath(image.getOriginalFilename());
    String extenssao = StringUtils.getFilenameExtension(image.getOriginalFilename());
    fileName = fileName.substring(0, fileName.lastIndexOf("."));
    usuario.setImagem(fileName);
    usuario.setExtensao(extenssao);
    validarDadosUsuario(usuario.getImagem(), usuario);
    usuario.setClienteSistema(usuarioLogado().getClienteSistema());
    usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
    usuarioRepository.save(usuario);
    salvarImagemStorage(image, fileName);
  }

  public static Usuario usuarioLogado() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Usuario usuario = ((UsuarioSistema) authentication.getPrincipal()).getUsuario();
    return usuario;
  }
  
  public Optional<Usuario> findByEmailAndAtivoTrue(String email) {
    return usuarioRepository.findByEmailAndAtivoTrue(email);
  }
  
  public Optional<Usuario> findByClienteSistemaTenantAndId(String tenant, Long id) {
    return usuarioRepository.findByClienteSistemaTenantAndId(tenant, id).map(user -> {
      return Optional.of(user);
    }).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nenhum usuário encontrado!"));
  }
  
  public List<UsuarioMensagemdto> buscarUsuariosPorTenant() {
    var usuarioLogado = usuarioLogado();
    var filtroUsuariosPorTenant = usuarioRepository.findAllByAtivoTrueAndClienteSistemaTenantAndEmailNotLike(usuarioLogado.getClienteSistema().getTenant(), usuarioLogado.getEmail());
    List<UsuarioMensagemdto> usuariodtos = new ArrayList<>();
    filtroUsuariosPorTenant.forEach(usuario -> {
      var usuariodto = new UsuarioMensagemdto();
      usuariodto.setId(usuario.getId());
      usuariodto.setText(usuarioLogado.getClienteSistema().getTenant());
      usuariodto.setNome(usuarioLogado.getClienteSistema().getNomeComercio());
      usuariodtos.add(usuariodto);
    });
    return usuariodtos;
  }
  
  public List<Usuario> filtrar(UsuarioFiltro usuarioFiltro) {
    return usuarioRepository.filtrar(usuarioFiltro.getNome(), usuarioFiltro.getEmail());
  }
  
  private void validarDadosUsuario(String nomeImagem, Usuario usuario) {
    existeTelefone(usuario.getTelefone());
    existeEmail(usuario.getEmail());
    existeNomeImagem(nomeImagem);
    permiteCriarUsuario();
    validarEmail(usuario.getEmail());
  }
  
  private void existeTelefone(String telefone) {
    if(usuarioRepository.existeTelefone(org.apache.commons.lang3.StringUtils.getDigits(telefone))) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não é permitido criar uma conta de usuário com esse telefone!");
    }
  }
  
  private void permiteCriarUsuario() {
    if(!usuarioRepository.permiteCriarUsuario()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Limite de usuário excedido, atualmente você pode ter: "+usuarioLogado().getClienteSistema().getQtdUsuario());
    }
  }
  
  private void existeNomeImagem(String nomeImagem) {
    if(usuarioRepository.findByImagemAndClienteSistemaTenant(nomeImagem, usuarioLogado().getClienteSistema().getTenant()).isPresent()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Por favor altere o nome da imagem!");
    }
  }
  
  private void existeEmail(String email) {
    if (usuarioRepository.existeEmail(email)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não é permitido criar uma conta de usuário com esse email!");
    }
  }
  
  private void validarEmail(String email) {
    log.info("Validando emial!");
    String url = "https://isitarealemail.com/api/email/validate?email=".concat(email);
    EmailValido emailValido = restTemplate.getForObject(url, EmailValido.class);
    if(Objects.nonNull(emailValido)) {
      if(!"valid".equalsIgnoreCase(emailValido.getStatus())) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email inválido!");
      }
    }
  }
  
  private void salvarImagemStorage(MultipartFile image, String fileName) {
    try {
      storageCloudnary.uploadFotoPerfil(image.getBytes(), fileName);
    } catch (IOException ex) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage());
    }
  }
  
}
