
package com.store.drinks.entidade.dto.meli;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.Data;

@Data
public class RevogarContaMelidto implements Serializable {
  @JsonProperty("user_id")
  private String userId;
  @JsonProperty("app_id")
  private String appId;
  private String msg;
}
