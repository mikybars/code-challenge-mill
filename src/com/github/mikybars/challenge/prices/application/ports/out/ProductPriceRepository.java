package com.github.mikybars.challenge.prices.application.ports.out;

import com.github.mikybars.challenge.prices.application.ProductPriceSearchCriteria;
import com.github.mikybars.challenge.prices.domain.ProductPrice;
import java.util.Optional;

public interface ProductPriceRepository {

  Optional<ProductPrice> findTheHighestRankedBy(
      ProductPriceSearchCriteria searchCriteria);
}
