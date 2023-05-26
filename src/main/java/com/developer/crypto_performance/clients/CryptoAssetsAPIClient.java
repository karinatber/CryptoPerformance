package com.developer.crypto_performance.clients;

import com.developer.crypto_performance.models.Asset;
import com.developer.crypto_performance.models.AssetsAPIResponse;
import com.developer.crypto_performance.models.PriceHistory;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

public interface CryptoAssetsAPIClient {
  // setting User agent headers in order to not receive 403-Forbidden from API servers

  @Headers(value = {"User-Agent: PostmanRuntime/7.32.2"})
  @RequestLine("GET /assets?search={searchText}&limit={limit}")
  AssetsAPIResponse<Asset> assetsBySearch(@Param("searchText") String searchText,
      @Param("limit") int limit);

  @Headers(value = {"User-Agent: PostmanRuntime/7.32.2"})
  @RequestLine("GET /assets/{cryptoId}/history?interval={interval}&start={start}&end={end}")
  AssetsAPIResponse<PriceHistory> assetPriceHistory(@Param("cryptoId") String cryptoId,
      @Param("interval") String interval, @Param("start") String start, @Param("end") String end);
}
