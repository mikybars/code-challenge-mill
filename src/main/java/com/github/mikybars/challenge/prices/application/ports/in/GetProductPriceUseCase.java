package com.github.mikybars.challenge.prices.application.ports.in;

import com.github.mikybars.challenge.prices.application.ProductPriceSearchCriteria;
import com.github.mikybars.challenge.prices.domain.ProductPrice;

public interface GetProductPriceUseCase {

  ProductPrice execute(ProductPriceSearchCriteria searchCriteria);
}
