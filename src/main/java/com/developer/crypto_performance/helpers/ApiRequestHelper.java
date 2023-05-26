package com.developer.crypto_performance.helpers;

import javax.annotation.Nonnull;
import feign.Feign;
import feign.gson.GsonDecoder;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Nonnull
public class ApiRequestHelper {

  private String baseUrl;

  public <T> T getClient(Class<T> clazz) {
    T client = Feign.builder().decoder(new GsonDecoder()).target(clazz, this.baseUrl);

    return client;
  }
}
