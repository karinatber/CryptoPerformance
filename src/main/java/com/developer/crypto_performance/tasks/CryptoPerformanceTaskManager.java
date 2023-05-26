package com.developer.crypto_performance.tasks;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.developer.crypto_performance.models.CryptoWalletItem;
import com.developer.crypto_performance.models.WalletItemPerformance;
import com.developer.crypto_performance.models.WalletPerformance;
import com.developer.crypto_performance.services.CsvReaderService;
import com.developer.crypto_performance.services.PerformanceService;

@Nonnull
public class CryptoPerformanceTaskManager {
  private static final Logger LOGGER = LoggerFactory.getLogger(CryptoPerformanceTaskManager.class);

  @Autowired
  private CsvReaderService csvReaderService;

  @Autowired
  private PerformanceService performanceService;



  public void init() {
    LOGGER.info("Starting Task Manager process");
    try {
      List<CryptoWalletItem> cryptos =
          csvReaderService.read("crypto-wallet.csv", CryptoWalletItem.class);

      List<WalletItemPerformance> analyzedCryptos = new ArrayList<>();

      int index = -1;

      CryptoPerformanceTask task = new CryptoPerformanceTask(performanceService);
      CompletableFuture<Void> worker1 = null;
      CompletableFuture<Void> worker2 = null;
      CompletableFuture<Void> worker3 = null;

      Consumer<? super WalletItemPerformance> callback = itemPerf -> {
        analyzedCryptos.add(itemPerf);
      };

      while (analyzedCryptos.size() != cryptos.size()) {
        if (index + 1 >= cryptos.size()) {
          continue;
        }
        if (worker1 == null || worker1.isDone()) {
          index = index < 0 ? 0 : index + 1;
          worker1 = task.startAnalysis(cryptos.get(index)).thenAccept(callback);
          continue;
        }
        if (worker2 == null || worker2.isDone()) {
          index += 1;
          worker2 = task.startAnalysis(cryptos.get(index)).thenAccept(callback);
          continue;

        }
        if (worker3 == null || worker3.isDone()) {
          index += 1;
          worker3 = task.startAnalysis(cryptos.get(index)).thenAccept(callback);
        }
      }
      LOGGER.info("Finished processing cryptos from wallet");

      LOGGER.info("Calculating overall wallet performance");
      WalletPerformance walletPerf =
          performanceService.calculateOverallPerformance(analyzedCryptos);

      LOGGER.info(walletPerf.toString());

    } catch (Exception e) {
      LOGGER.error("Error occurred on task manager process", e);
    }
  }
}
