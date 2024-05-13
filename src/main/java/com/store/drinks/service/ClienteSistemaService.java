package com.store.drinks.service;

import com.store.drinks.entidade.ClienteSistema;
import com.store.drinks.repository.ClienteSistemaRepository;
import com.store.drinks.storage.StorageCloudnary;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClienteSistemaService {

  private final ClienteSistemaRepository clienteSistemaRepository;
  private final StorageCloudnary storageCloudnary;

  @PreAuthorize("hasRole('SUPER_USER')")
  @Transactional
  public void salvar(ClienteSistema clienteSistema) {
    clienteSistema.setDataCadastro(LocalDateTime.now());
    clienteSistema.setDataAtualizacao(clienteSistema.getDataCadastro());
    clienteSistemaRepository.save(clienteSistema);
    storageCloudnary.createFolder(clienteSistema.getTenant());
  }

  public Optional<ClienteSistema> buscarPorCpfCnpj(String cpfCnpj) {
    return clienteSistemaRepository.findByCpfCnpj(cpfCnpj);
  }
}
