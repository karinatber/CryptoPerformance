package com.developer.crypto_performance;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.developer.crypto_performance.errors.FileNotFoundException;
import com.developer.crypto_performance.models.CryptoWalletItem;
import com.developer.crypto_performance.services.CsvReaderService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class CsvReaderServiceTest extends BaseTest {

  @Autowired
  private CsvReaderService csvReaderService;

  @Test
  public void testReadSuccesfull() throws IOException, URISyntaxException {
    List<CryptoWalletItem> obtainedWallet =
        csvReaderService.read(csvSample, CryptoWalletItem.class);
    Assert.assertEquals(mockWallet, obtainedWallet);
  }

  @Test(expected = FileNotFoundException.class)
  public void testReadNonExistantFile() throws IOException, URISyntaxException {
    csvReaderService.read("non-existing.csv", CryptoWalletItem.class);
  }
}
