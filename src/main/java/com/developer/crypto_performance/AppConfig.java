package com.developer.crypto_performance;

import static com.developer.crypto_performance.constants.Constants.BASE_API_URL;
import javax.annotation.Nonnull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.developer.crypto_performance.helpers.ApiRequestHelper;
import com.developer.crypto_performance.services.CsvReaderService;
import com.developer.crypto_performance.services.PerformanceService;
import com.developer.crypto_performance.tasks.CryptoPerformanceTaskManager;

@Configuration
@Nonnull
public class AppConfig {

  @Bean
  CsvReaderService csvReaderService() {
    return CsvReaderService.builder().build();
  }
  
  @Bean
  ApiRequestHelper apiRequestHelper() {
    return ApiRequestHelper.builder().baseUrl(BASE_API_URL).build();
  }
  
  @Bean
  CryptoPerformanceTaskManager cryptoPerformanceTaskManager() {
    return new CryptoPerformanceTaskManager();
  }
  
  @Bean
  PerformanceService performanceService() {
    return new PerformanceService();
  }

}
