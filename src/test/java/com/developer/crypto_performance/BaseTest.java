package com.developer.crypto_performance;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import org.junit.Before;
import com.developer.crypto_performance.models.CryptoWalletItem;

@Nonnull
public class BaseTest {

  protected String csvSample = "wallet-sample.csv";
  protected List<CryptoWalletItem> mockWallet;

  protected float BTC_POSITION = (float) 0.12345;
  protected float ETH_POSITION = (float) 4.89532;

  protected float BTC_CURRENT_POSITION = (float) 7036.647;
  protected float ETH_CURRENT_POSITION = (float) 9947.973;

  protected float BTC_CURRENT_PRICE = (float) 56999.973;
  protected float ETH_CURRENT_PRICE = (float) 2032.139;

  protected float BTC_CURRENT_PERFORMANCE = (float) 1.5051284;
  protected float ETH_CURRENT_PERFORMANCE = (float) 1.0135473;

  @Before
  public void setUp() {
    mockWallet = buildMockCryptoWallet();
  }

  private CryptoWalletItem buildMockWalletItem(String symbol, Float quantity, Float price) {
    return CryptoWalletItem.builder().symbol(symbol).quantity(quantity).price(price).build();
  }

  private List<CryptoWalletItem> buildMockCryptoWallet() {
    List<CryptoWalletItem> wallet = new ArrayList<>();
    wallet.add(buildMockWalletItem("BTC", BTC_POSITION, (float) 37870.5058));
    wallet.add(buildMockWalletItem("ETH", ETH_POSITION, (float) 2004.9774));

    return wallet;
  }
}
