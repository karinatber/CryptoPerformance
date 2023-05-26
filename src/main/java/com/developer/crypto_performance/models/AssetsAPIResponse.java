package com.developer.crypto_performance.models;

import java.math.BigInteger;
import java.util.List;
import lombok.Data;

@Data
public class AssetsAPIResponse<T> {

  List<T> data;

  BigInteger timestamp;
}
