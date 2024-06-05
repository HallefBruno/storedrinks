
package com.store.drinks.entidade.dto.meli;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
public class AccessTokenMelidto {
  
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
