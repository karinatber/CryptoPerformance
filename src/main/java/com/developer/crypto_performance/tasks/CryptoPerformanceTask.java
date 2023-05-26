package com.developer.crypto_performance.tasks;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.developer.crypto_performance.models.CryptoWalletItem;
import com.developer.crypto_performance.models.WalletItemPerformance;
import com.developer.crypto_performance.services.PerformanceService;
import lombok.Data;

@Nonnull
@Data
public class CryptoPerformanceTask {
  private static final Logger LOGGER = LoggerFactory.getLogger(CryptoPerformanceTask.class);

  PerformanceService performanceService;

  public CryptoPerformanceTask(PerformanceService performanceService) {
    this.performanceService = performanceService;
  }

  public CompletableFuture<WalletItemPerformance> startAnalysis(CryptoWalletItem walletItem) {
    WalletItemPerformance fallbackItem =
        WalletItemPerformance.builder().walletItem(walletItem).build();

    CompletableFuture<WalletItemPerformance> task = CompletableFuture.supplyAsync(() -> {
      if (walletItem == null) {
        return null;
      }
      LOGGER.info("Starting task for " + walletItem.getSymbol());
      return performanceService.checkPerformance(walletItem);
    }).completeOnTimeout(fallbackItem, 60, TimeUnit.SECONDS);

    task = task.handle((itemPerf, ex) -> {
      if (itemPerf != null) {
        LOGGER.info("Finished task for " + walletItem.getSymbol());
        return itemPerf;
      }

      if (ex != null) {
        LOGGER.error(String.format("There was an error retrieving performance info for %s",
            walletItem.getSymbol()), ex);
      }
      return fallbackItem;
    });

    return task;
  }
}
