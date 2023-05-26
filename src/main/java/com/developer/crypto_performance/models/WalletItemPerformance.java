package com.developer.crypto_performance.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WalletItemPerformance {

  Asset assetInfo;

  PriceHistory referencePrice;

  CryptoWalletItem walletItem;

  Float currentPosition;

  Float performance;
}
