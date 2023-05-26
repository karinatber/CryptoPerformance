package com.developer.crypto_performance.errors;

public class FileNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public FileNotFoundException(String message) {
    super(message);
  }

}
