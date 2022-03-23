package com.store.drinks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class StoredrinksApplication {

  public static void main(String[] args) throws Exception {
    SpringApplication.run(StoredrinksApplication.class, args);
  }
}
