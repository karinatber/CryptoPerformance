package com.developer.crypto_performance;

import static org.junit.Assert.assertEquals;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.developer.crypto_performance.models.Asset;
import com.developer.crypto_performance.models.CryptoWalletItem;
import com.developer.crypto_performance.models.PriceHistory;
import com.developer.crypto_performance.models.WalletItemPerformance;
import com.developer.crypto_performance.models.WalletPerformance;
import com.developer.crypto_performance.services.PerformanceService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class PerformanceServiceTest extends BaseTest {

  @Autowired
  private PerformanceService performanceService;

  @Test
  public void testCheckPerformance() {
    CryptoWalletItem walletItem = this.mockWallet.get(0);
    WalletItemPerformance expected = WalletItemPerformance.builder().walletItem(walletItem)
        .assetInfo(buildAssetInfo("bitcoin", "Bitcoin", "BTC")).currentPosition((float) 7036.647)
        .performance((float) 1.5051284).referencePrice(buildPriceHistory((float) 56999.973))
        .build();

    WalletItemPerformance result = performanceService.checkPerformance(walletItem);

    assertEquals("Result performance item does not match expected performance", expected, result);
  }

  @Test
  public void testCalculateOverallPerformance() {
    List<WalletItemPerformance> mockItemPerfs = buildListOfItemPerfs();

    WalletPerformance expected = WalletPerformance.builder().total((float) 16984.62)
        .bestAsset("BTC").bestPerformance((float) 1.5051284).worstAsset("ETH")
        .worstPerformance((float) 1.0135473).build();

    WalletPerformance result = performanceService.calculateOverallPerformance(mockItemPerfs);
    assertEquals("Wallet performance does not match with expected", expected, result);

    String expectedMsg =
        "total=16984.62,best_asset=BTC,best_performance=1.51,worst_asset=ETH,worst_performance=1.01";
    assertEquals("Actual message does not match with expected", expectedMsg, result.toString());
  }

  private PriceHistory buildPriceHistory(float price) {
    return PriceHistory.builder().priceUsd(price).date("2021-04-07T00:00:00.000Z")
        .time(new BigInteger("1617753600000")).build();
  }

  private Asset buildAssetInfo(String id, String name, String symbol) {
    return Asset.builder().id(id).name(name).symbol(symbol).build();
  }

  private List<WalletItemPerformance> buildListOfItemPerfs() {
    WalletItemPerformance item1 = WalletItemPerformance.builder().walletItem(this.mockWallet.get(0))
        .assetInfo(buildAssetInfo("bitcoin", "Bitcoin", "BTC"))
        .currentPosition(BTC_CURRENT_POSITION).performance(BTC_CURRENT_PERFORMANCE)
        .referencePrice(buildPriceHistory(BTC_CURRENT_PRICE)).build();

    WalletItemPerformance item2 = WalletItemPerformance.builder().walletItem(this.mockWallet.get(1))
        .assetInfo(buildAssetInfo("ethereum", "Ethereum", "ETH"))
        .currentPosition(ETH_CURRENT_POSITION).performance(ETH_CURRENT_PERFORMANCE)
        .referencePrice(buildPriceHistory(ETH_CURRENT_PRICE)).build();

    List<WalletItemPerformance> itemPerfs = new ArrayList<>();
    itemPerfs.add(item1);
    itemPerfs.add(item2);

    return itemPerfs;
  }
}
