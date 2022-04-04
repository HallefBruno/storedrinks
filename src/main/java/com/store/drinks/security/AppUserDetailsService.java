package com.store.drinks.security;

import com.store.drinks.entidade.Usuario;
import com.store.drinks.repository.UsuarioRepository;
import com.store.drinks.service.LoginAttemptService;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequiredArgsConstructor
@Slf4j
public class AppUserDetailsService implements UserDetailsService {

  private final UsuarioRepository usuarioRepository;
  private final LoginAttemptService loginAttemptService;
  private final HttpServletRequest request;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    String ip = getClientIP();
    if (loginAttemptService.isBlocked(ip)) {
      request.setAttribute("UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Acesso bloqueado!");
    }
    Optional<Usuario> usuarioOptional = usuarioRepository.findByUserLogin(email);
    Usuario usuario = usuarioOptional.orElseThrow(() -> new UsernameNotFoundException("Usuário e/ou senha incorretos"));
    return new UsuarioSistema(usuario, getPermissoes(usuario));
  }

  private Collection<? extends GrantedAuthority> getPermissoes(Usuario usuario) {
    Set<SimpleGrantedAuthority> authorities = new HashSet<>();
    List<String> permissoes = usuarioRepository.permissoes(usuario);
    permissoes.forEach(p -> authorities.add(new SimpleGrantedAuthority(p.toUpperCase())));
    return authorities;
  }

  private String getClientIP() {
    String xfHeader = request.getHeader("X-Forwarded-For");
    if (Objects.isNull(xfHeader)) {
      return request.getRemoteAddr();
    }
    return xfHeader.split(",")[0];
  }

}
