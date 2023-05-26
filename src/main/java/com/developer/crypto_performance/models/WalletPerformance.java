package com.developer.crypto_performance.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WalletPerformance {

  Float total;

  String bestAsset;

  Float bestPerformance;

  String worstAsset;

  Float worstPerformance;

  public String toString() {

    return String.format(
        "total=%.2f,best_asset=%s,best_performance=%.2f,worst_asset=%s,worst_performance=%.2f",
        total, bestAsset, bestPerformance, worstAsset, worstPerformance);
  }
}
