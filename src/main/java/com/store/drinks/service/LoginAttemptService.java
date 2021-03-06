package com.store.drinks.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Service;

@Service
public class LoginAttemptService {

  private final int MAX_ATTEMPT = 3;
  private final LoadingCache<String, Integer> attemptsCache;

  public LoginAttemptService() {
    super();
    attemptsCache = CacheBuilder.newBuilder().expireAfterWrite(5, TimeUnit.MINUTES).build(new CacheLoader<String, Integer>() {
      @Override
      public Integer load(String key) {
        return 0;
      }
    });
  }

  public void loginSucceeded(String key) {
    attemptsCache.invalidate(key);
  }

  public void loginFailed(String key) {
    int attempts;
    try {
      attempts = attemptsCache.get(key);
    } catch (ExecutionException e) {
      attempts = 0;
    }
    attempts++;
    attemptsCache.put(key, attempts);
  }

  public boolean isBlocked(String key) {
    try {
      return attemptsCache.get(key) >= MAX_ATTEMPT;
    } catch (ExecutionException e) {
      return false;
    }
  }
}
