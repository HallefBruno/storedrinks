
package com.store.drinks.entidade.dto.meli;

import java.io.Serializable;
import lombok.Data;

@Data
public class RevogarContaMelidto implements Serializable {
  private String userId;
  private String appId;
  private String msg;
}
