package com.developer.crypto_performance.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.developer.crypto_performance.errors.FileNotFoundException;
import com.developer.crypto_performance.models.CsvItem;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.Builder;

@Nonnull
@Builder
public class CsvReaderService {
  private static final Logger LOGGER = LoggerFactory.getLogger(CsvReaderService.class);

  String apiURl;

  public <T extends CsvItem> List<T> read(String csvFileName, Class<T> clazz)
      throws IOException, URISyntaxException {
    List<T> builtItems = new ArrayList<>();
    Enumeration<URL> resList = ClassLoader.getSystemResources(csvFileName);
    LOGGER.info("getSystemResources: {}", resList);
    URL res = ClassLoader.getSystemClassLoader().getResource(csvFileName);
    LOGGER.info("getResource: {}", res);

    InputStream is = this.getClass().getClassLoader().getResourceAsStream(csvFileName);
    if (is != null) {
      LOGGER.info("Found resource stream");
    }


    // URL pathUrl = ClassLoader.getSystemResource(csvFileName);
    if (is == null) {
      throw new FileNotFoundException("File could not be found");
    }
    // LOGGER.info("Reading csv file from {}", pathUrl.toString());
    // Path builtPath = Paths.get(pathUrl.toURI());


    // try (Reader reader = Files.newBufferedReader(builtPath, Charsets.toCharset("UTF-8"))) {
    try (Reader reader = new BufferedReader(new InputStreamReader(is))) {
      CsvToBean<T> cb = new CsvToBeanBuilder<T>(reader).withType(clazz).build();

      builtItems = cb.parse();
      reader.close();
    }
    return builtItems;
  }


}
