package com.developer.crypto_performance.models;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CryptoWalletItem implements CsvItem {

  @CsvBindByName
  private String symbol;

  @CsvBindByName
  private Float quantity;

  @CsvBindByName
  private Float price;

}
