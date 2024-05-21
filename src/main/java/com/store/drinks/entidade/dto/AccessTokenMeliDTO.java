
package com.store.drinks.entidade.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class AccessTokenMeliDTO {
  
  @JsonProperty("access_token")
  private String accessToken;

  @JsonProperty("token_type")
  private String tokenType;

  @JsonProperty("expires_in")
  private Integer expiresIn;

  @JsonProperty("scope")
  private String scope;

  @JsonProperty("user_id")
  private Long userId;

  @JsonProperty("refresh_token")
  private String refreshToken;
  
}
