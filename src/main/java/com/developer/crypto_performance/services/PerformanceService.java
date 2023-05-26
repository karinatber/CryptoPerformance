package com.developer.crypto_performance.services;

import static com.developer.crypto_performance.constants.Constants.API_HISTORY_END_QUERY;
import static com.developer.crypto_performance.constants.Constants.API_HISTORY_INTERVAL_QUERY;
import static com.developer.crypto_performance.constants.Constants.API_HISTORY_START_QUERY;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.developer.crypto_performance.clients.CryptoAssetsAPIClient;
import com.developer.crypto_performance.helpers.ApiRequestHelper;
import com.developer.crypto_performance.models.Asset;
import com.developer.crypto_performance.models.AssetsAPIResponse;
import com.developer.crypto_performance.models.CryptoWalletItem;
import com.developer.crypto_performance.models.PriceHistory;
import com.developer.crypto_performance.models.WalletItemPerformance;
import com.developer.crypto_performance.models.WalletPerformance;

@Service
public class PerformanceService {
  private static final Logger LOGGER = LoggerFactory.getLogger(PerformanceService.class);

  @Autowired
  private ApiRequestHelper apiRequestHelper;

  public WalletItemPerformance checkPerformance(CryptoWalletItem walletItem) {
    CryptoAssetsAPIClient client = apiRequestHelper.getClient(CryptoAssetsAPIClient.class);
    AssetsAPIResponse<Asset> assetResponse = client.assetsBySearch(walletItem.getSymbol(), 1);
    Asset assetInfo = assetResponse.getData().get(0);
    String cryptoId = assetInfo.getId();

    WalletItemPerformance walletPerf = WalletItemPerformance.builder().assetInfo(assetInfo)
        .walletItem(walletItem).currentPosition((float) 0.0).performance(null).build();

    AssetsAPIResponse<PriceHistory> priceHistoryResponse = client.assetPriceHistory(cryptoId,
        API_HISTORY_INTERVAL_QUERY, API_HISTORY_START_QUERY, API_HISTORY_END_QUERY);
    if (priceHistoryResponse.getData().isEmpty()) {
      LOGGER.info(String.format("There is no asset history for %s", walletItem.getSymbol()));
      return walletPerf;
    }
    PriceHistory priceHistory = priceHistoryResponse.getData().get(0);
    Float price = priceHistory.getPriceUsd();

    Float currentPosition = walletItem.getQuantity() * price;

    Float priceDiff = price - walletItem.getPrice();
    Float performance = 1 + (priceDiff / walletItem.getPrice());

    walletPerf.setReferencePrice(priceHistory);
    walletPerf.setCurrentPosition(currentPosition);
    walletPerf.setPerformance(performance);

    return walletPerf;
  }

  public WalletPerformance calculateOverallPerformance(List<WalletItemPerformance> itemsPerfs) {
    Float total = (float) 0.0;

    WalletItemPerformance firstItem = itemsPerfs.get(0);

    String worstAsset = firstItem.getAssetInfo().getSymbol();
    Float worstPerf = firstItem.getPerformance() != null ? firstItem.getPerformance() : (float) 0.0;

    String bestAsset = firstItem.getAssetInfo().getSymbol();
    Float bestPerf = firstItem.getPerformance() != null ? firstItem.getPerformance() : (float) 0.0;

    final WalletPerformance walletPerf =
        WalletPerformance.builder().bestAsset(bestAsset).bestPerformance(bestPerf).total(total)
            .worstAsset(worstAsset).worstPerformance(worstPerf).build();

    itemsPerfs.forEach(item -> {
      if (item.getReferencePrice() == null || item.getAssetInfo() == null) {
        LOGGER.info(String.format("There is not enough information for %s",
            item.getWalletItem().getSymbol()));
        return;
      }
      walletPerf.setTotal(walletPerf.getTotal() + item.getCurrentPosition());
      Float performance = item.getPerformance();
      if (performance == null) {
        return;
      }

      if (performance < walletPerf.getWorstPerformance()) {
        walletPerf.setWorstPerformance(item.getPerformance());
        walletPerf.setWorstAsset(item.getAssetInfo().getSymbol());
      } else if (performance > walletPerf.getBestPerformance()) {
        walletPerf.setBestPerformance(item.getPerformance());
        walletPerf.setBestAsset(item.getAssetInfo().getSymbol());
      }
    });

    return walletPerf;
  }
}
