package com.store.drinks.service;

import com.store.drinks.entidade.Grupo;
import com.store.drinks.entidade.Permissao;
import com.store.drinks.entidade.Usuario;
import com.store.drinks.entidade.dto.EmailValido;
import com.store.drinks.entidade.dto.usuario.UsuarioMensagemdto;
import com.store.drinks.entidade.enuns.PermissaoSu;
import com.store.drinks.repository.UsuarioRepository;
import com.store.drinks.repository.filtros.UsuarioFiltro;
import com.store.drinks.security.UsuarioSistema;
import com.store.drinks.storage.StorageCloudnary;
import com.store.drinks.util.CopyProperties;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
  
  private RestTemplate restTemplate = new RestTemplateBuilder().build();
  private UsuarioRepository usuarioRepository;
  private StorageCloudnary storageCloudnary;
  private PasswordEncoder passwordEncoder;

  @Transactional
  public Usuario salvar(Usuario usuario) {
    return usuarioRepository.save(usuario);
  }
  
  @Transactional
  public void salvar(Usuario usuario, MultipartFile image) {
    if (Objects.isNull(usuario.getSenha())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Senha é obrigatória");
    } else if (usuario.getSenha().length() < 11) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Senha precisa ter no mínimo 11 caracteres");
    }
    String fileName = StringUtils.cleanPath(image.getOriginalFilename());
    String extenssao = StringUtils.getFilenameExtension(image.getOriginalFilename());
    fileName = fileName.substring(0, fileName.lastIndexOf("."));
    usuario.setImagem(fileName);
    usuario.setExtensao(extenssao);
    validarDadosUsuarioSalvar(usuario);
    usuario.setClienteSistema(usuarioLogado().getClienteSistema());
    usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
    usuarioRepository.save(usuario);
    salvarImagemStorage(image, fileName);
  }
  
  @Transactional
  public void update(Usuario usuarioUpdate, Long codigo, MultipartFile image) {
    if (Objects.isNull(codigo)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Identificador inválido!");
    }
    
    usuarioRepository.findById(codigo).map(usuarioAtual -> {
      String fileName = "";
      String extensao = "";
      if (image.isEmpty() && org.apache.commons.lang3.StringUtils.isBlank(usuarioAtual.getImagem())) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Selecione uma imagem!");
      } else if (!image.isEmpty()) {
        fileName = StringUtils.cleanPath(image.getOriginalFilename());
        fileName = fileName.substring(0, fileName.lastIndexOf("."));
        extensao = StringUtils.getFilenameExtension(image.getOriginalFilename());
      } else if (image.isEmpty() && !org.apache.commons.lang3.StringUtils.isBlank(usuarioAtual.getImagem())) {
        fileName = usuarioAtual.getImagem();
        extensao = usuarioAtual.getExtensao();
      }
      CopyProperties.copyProperties(usuarioUpdate, usuarioAtual, "id", "telefone", "imagem", "extensao", "senha");
      casoAltereSenha(usuarioUpdate, usuarioAtual);
      verificaAtributoNull(usuarioUpdate, usuarioAtual);
      validarDadosUsuarioUpdate(usuarioUpdate, usuarioAtual);
      usuarioAtual.setTelefone(usuarioUpdate.getTelefone());
      usuarioAtual.setImagem(fileName);
      usuarioAtual.setExtensao(extensao);
      usuarioRepository.save(usuarioAtual);
      usuarioLogado().setImagem(usuarioAtual.getImagem());
      salvarImagemStorage(image, fileName);
      return Void.TYPE;
    }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhum resultado encontrado para o identificador: "+codigo));
  }

  public Usuario usuarioLogado() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Usuario usuario = ((UsuarioSistema) authentication.getPrincipal()).getUsuario();
    usuario.setAtivo(usuarioAtivo());
    Authentication newAuth = new UsernamePasswordAuthenticationToken(usuario, authentication.getCredentials(), authentication.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(newAuth);
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
    String tenant = usuarioLogado().getClienteSistema().getTenant();
    for(Grupo g : usuarioLogado().getGrupos()) {
      for (Permissao p : g.getPermissoes()) {
        if(PermissaoSu.SUPER_USER.get().equalsIgnoreCase(p.getNome())) {
          return usuarioRepository.filtrar(usuarioFiltro.getNome(), usuarioFiltro.getEmail(), null);
        }
      }
    }
    return usuarioRepository.filtrar(usuarioFiltro.getNome(), usuarioFiltro.getEmail(), tenant);
  }
  
  private void validarDadosUsuarioSalvar(Usuario usuario) {
    existeTelefone(usuario.getTelefone());
    existeEmail(usuario.getEmail());
    existeNomeImagem(usuario.getImagem());
    permiteCriarUsuario();
    validarEmail(usuario.getEmail());
  }
  
  private void validarDadosUsuarioUpdate(Usuario usuarioUpdate, Usuario usuarioAtual) {
    String telefoneUpdate = org.apache.commons.lang3.StringUtils.getDigits(usuarioUpdate.getTelefone());
    String telefoneAtual = org.apache.commons.lang3.StringUtils.getDigits(usuarioAtual.getTelefone());
    if (!telefoneUpdate.equals(telefoneAtual)) {
      existeTelefone(usuarioUpdate.getTelefone());
    }
    if (!usuarioUpdate.getEmail().equalsIgnoreCase(usuarioAtual.getEmail())) {
      existeEmail(usuarioUpdate.getEmail());
    }
    if (!org.apache.commons.lang3.StringUtils.isBlank(usuarioUpdate.getImagem()) && !org.apache.commons.lang3.StringUtils.isBlank(usuarioAtual.getImagem())) {
      if (!usuarioUpdate.getImagem().equalsIgnoreCase(usuarioAtual.getImagem())) {
        existeNomeImagem(usuarioUpdate.getImagem());
      }
    }
    validarEmail(usuarioUpdate.getEmail());
  }
  
  private void existeTelefone(String telefone) {
    if (usuarioRepository.existeTelefone(org.apache.commons.lang3.StringUtils.getDigits(telefone))) {
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
  
  private void verificaAtributoNull(Usuario usuarioUpdate, Usuario usuarioAtual) {
    if (Objects.isNull(usuarioUpdate.getAtivo())) {
      usuarioAtual.setAtivo(Boolean.FALSE);
    }
    if (Objects.isNull(usuarioUpdate.getProprietario())) {
      usuarioAtual.setProprietario(Boolean.FALSE);
    }
  }
  
  private void casoAltereSenha(Usuario usuarioUpdate, Usuario usuarioAtual) {
    if (!org.apache.commons.lang3.StringUtils.isBlank(usuarioUpdate.getSenha())) {
      usuarioAtual.setSenha(passwordEncoder.encode(usuarioUpdate.getSenha()));
    }
  }
  
  private void salvarImagemStorage(MultipartFile image, String fileName) {
    try {
      if(!image.isEmpty()) {
        storageCloudnary.uploadFotoPerfil(image.getBytes(), fileName);
      }
    } catch (IOException ex) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage());
    }
  }
  
  private Boolean usuarioAtivo() {
    return usuarioRepository.usuarioAtivo(usuarioLogado().getId());
  }
}
