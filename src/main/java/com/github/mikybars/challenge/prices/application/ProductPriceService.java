package com.github.mikybars.challenge.prices.application;

import com.github.mikybars.challenge.common.NotFoundException;
import com.github.mikybars.challenge.prices.application.ports.in.GetProductPriceUseCase;
import com.github.mikybars.challenge.prices.application.ports.out.ProductPriceRepository;
import com.github.mikybars.challenge.prices.domain.ProductPrice;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class ProductPriceService implements GetProductPriceUseCase {

  private final ProductPriceRepository productPriceRepository;

  @Override
  public ProductPrice execute(ProductPriceSearchCriteria searchCriteria) {
    return productPriceRepository
        .findTheHighestRankedBy(searchCriteria)
        .orElseThrow(notFoundExceptionTriggeredBy(searchCriteria));
  }

  private static Supplier<NotFoundException> notFoundExceptionTriggeredBy(
      ProductPriceSearchCriteria criteria) {
    return () -> new NotFoundException("no product price found for %s".formatted(criteria));
  }
}
