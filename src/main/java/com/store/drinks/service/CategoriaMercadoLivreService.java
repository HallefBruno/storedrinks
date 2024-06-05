
package com.store.drinks.service;

import com.store.drinks.entidade.CategoriaMercadoLivre;
import com.store.drinks.entidade.dto.meli.CategoriaMercadoLivredto;
import com.store.drinks.repository.CategoriaMercadoLivreRepository;
import java.net.URI;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CategoriaMercadoLivreService {
  
  @Autowired
  private CategoriaMercadoLivreRepository categoriaMercadoLivreRepository;
  
  @Transactional
  public void salvar() {
    try {
      categoriaMercadoLivreRepository.deleteAll();
      RestTemplate restTemplate = new RestTemplate();
      ResponseEntity<List<CategoriaMercadoLivredto>> responseEntity = restTemplate.exchange(URI.create("https://api.mercadolibre.com/sites/MLB/categories"), HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<CategoriaMercadoLivredto>>() {});
      List<CategoriaMercadoLivredto> categoriasdto = responseEntity.getBody();
      ModelMapper modelMapper = new ModelMapper();
      List<CategoriaMercadoLivre> categorias = modelMapper.map(categoriasdto, new TypeToken<List<CategoriaMercadoLivre>>() {}.getType());
      categoriaMercadoLivreRepository.saveAll(categorias);
    } catch (RestClientException ex) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Um erro ocorreu ao atualizar categorias mercado livre");
    }
  }
  
  public List<CategoriaMercadoLivre> todas() {
    return categoriaMercadoLivreRepository.findAll();
  }
  
}
