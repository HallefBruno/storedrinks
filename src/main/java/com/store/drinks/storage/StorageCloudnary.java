package com.store.drinks.storage;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.store.drinks.execption.NegocioException;
import com.store.drinks.service.UsuarioService;
import java.io.IOException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Component
public class StorageCloudnary {

  @Value("${folder.foto-perfil}")
  private String folderFotoPerfil;
  
  private final static String PUBLIC_ID = "public_id";
  private final static String RESOURCE_TYPE = "resource_type";
  private final static String IMAGE = "image";
  private final static String INVALIDATE = "invalidate";
  private final static String TRUE = "true";

  public StorageCloudnary() {
    Cloudinary cloudinary = new Cloudinary(configOpenCloudinary());
    SingletonCloudinary.registerCloudinary(cloudinary);
  }

  private Map configOpenCloudinary() {
    Map chave = ObjectUtils.asMap(
"cloud_name", "storedrinks",
"api_key", "414869814418293",
"api_secret", "mWG1plNyyL8ufVQEiNNF9NnIcZw");
    return chave;
  }

  public void uploadFotoPerfil(byte[] dataImage, String nome) {
    try {
      if (!StringUtils.isBlank(nome)) {
        Map conf = ObjectUtils.asMap(PUBLIC_ID, urlParaFotoPerfil(nome),"quality_analysis", true, "transformation", new Transformation().height(110).width(110).gravity("auto").crop("fill").radius("max"));
        SingletonCloudinary.getCloudinary().uploader().upload(dataImage, conf);
        log.info("Upload da imagem completo!");
      } else {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome da imagem é obrigatório!");
      }
    } catch (IOException ex) {
      throw new NegocioException(HttpStatus.INTERNAL_SERVER_ERROR + " " + ex.getLocalizedMessage());
    }
  }

  public void deleteFotoPerfil(String nome) {
    try {
      if (!StringUtils.isBlank(nome)) {
        Map confCandidato = ObjectUtils.asMap(RESOURCE_TYPE, IMAGE, INVALIDATE, TRUE);
        SingletonCloudinary.getCloudinary().uploader().destroy(urlParaFotoPerfil(nome), confCandidato);
        log.info("Imagem deletada!");
      } else {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome da imagem é obrigatório!");
      }
    } catch (IOException ex) {
      throw new NegocioException(HttpStatus.INTERNAL_SERVER_ERROR + " " + ex.getLocalizedMessage());
    }
  }

  public void createFolder(String nome) {
    try {
      SingletonCloudinary.getCloudinary().api().createFolder(folderFotoPerfil.concat(nome), ObjectUtils.emptyMap());
      log.info("Pasta criada!");
    } catch (Exception ex) {
      throw new RuntimeException(ex.getLocalizedMessage());
    }
  }

  private String urlParaFotoPerfil(String nomeDaImagem) {
    return folderFotoPerfil.concat(tenant()).concat(nomeDaImagem);
  }

  private String tenant() {
    return UsuarioService.usuarioLogado().getClienteSistema().getTenant().concat("/");
  }

}
