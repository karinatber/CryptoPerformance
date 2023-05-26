package com.developer.crypto_performance;

import javax.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import com.developer.crypto_performance.tasks.CryptoPerformanceTaskManager;

@Nonnull
public class AppStarter {
  private static final Logger LOGGER = LoggerFactory.getLogger(AppStarter.class);

  public static void main(String[] args) {
    LOGGER.info("Starting up...");
    try (AnnotationConfigApplicationContext context =
        new AnnotationConfigApplicationContext(AppConfig.class)) {

      CryptoPerformanceTaskManager taskManager =
          context.getBean(CryptoPerformanceTaskManager.class);

      taskManager.init();

      LOGGER.info("Finished running App...");
    }
  }

}
