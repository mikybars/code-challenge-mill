package com.github.mikybars.challenge.prices;

import com.github.mikybars.challenge.prices.domain.BrandId;
import com.github.mikybars.challenge.prices.domain.Money;
import com.github.mikybars.challenge.prices.domain.PriceListId;
import com.github.mikybars.challenge.prices.domain.ProductId;
import com.github.mikybars.challenge.prices.domain.ProductPrice;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

public class ProductPrices {

  public static ProductPrice any() {
    return new ProductPrice(
        new ProductId("35455"), new BrandId("1"), new PriceListId("1"),
        LocalDateTime.parse("2020-06-14T00:00:00"),
        LocalDateTime.parse("2020-12-31T23:59:59"),
        new Money(new BigDecimal("35.50"), Currency.getInstance("EUR"))
    );
  }
}
