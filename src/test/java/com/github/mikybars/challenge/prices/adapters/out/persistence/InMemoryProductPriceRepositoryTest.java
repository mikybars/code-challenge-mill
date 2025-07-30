package com.github.mikybars.challenge.prices.adapters.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.mikybars.challenge.JpaConfiguration;
import com.github.mikybars.challenge.prices.application.ProductPriceSearchCriteria;
import com.github.mikybars.challenge.prices.domain.BrandId;
import com.github.mikybars.challenge.prices.domain.ProductId;
import com.github.mikybars.challenge.prices.domain.ProductPrice;
import java.time.LocalDateTime;
import java.util.Optional;
import org.approvaltests.JsonApprovals;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("standalone")
@Import({
    JpaConfiguration.class,
    InMemoryProductPriceRepository.class,
    ProductPricePersistenceMapperImpl.class
})
class InMemoryProductPriceRepositoryTest {

  static final LocalDateTime onlyPriceStartDate = LocalDateTime.parse("2020-06-14T00:00:00");
  static final LocalDateTime onlyPriceEndDate = LocalDateTime.parse("2020-12-31T23:59:59");
  static final LocalDateTime highestRankedStartDate = LocalDateTime.parse("2020-06-14T15:00:00");
  static final ProductId productWithOnePrice = new ProductId("35455");
  static final BrandId brandWithOnePrice = new BrandId("1");

  @Autowired
  InMemoryProductPriceRepository productPriceRepository;

  @Test
  void returnEmptyWhenDateIsBefore() {
    Optional<ProductPrice> productPrice = productPriceRepository.findTheHighestRankedBy(
        new ProductPriceSearchCriteria(onlyPriceStartDate.minusSeconds(1), productWithOnePrice
        ));

    assertThat(productPrice).isEmpty();
  }

  @Test
  void returnOneWhenDateEqualsStart() {
    Optional<ProductPrice> productPrice = productPriceRepository.findTheHighestRankedBy(
        new ProductPriceSearchCriteria(onlyPriceStartDate, productWithOnePrice));

    assertThat(productPrice).get()
        .satisfies(JsonApprovals::verifyAsJson);
  }

  @Test
  void returnOneWhenDateIsInBetween() {
    Optional<ProductPrice> productPrice = productPriceRepository.findTheHighestRankedBy(
        new ProductPriceSearchCriteria(onlyPriceStartDate.plusSeconds(1), productWithOnePrice));

    assertThat(productPrice).get()
        .extracting(ProductPrice::productId, ProductPrice::brandId)
        .containsExactly(productWithOnePrice, brandWithOnePrice);
  }

  @Test
  void returnOneWhenDateEqualsEnd() {
    Optional<ProductPrice> productPrice = productPriceRepository.findTheHighestRankedBy(
        new ProductPriceSearchCriteria(onlyPriceEndDate, productWithOnePrice));

    assertThat(productPrice).get()
        .extracting(ProductPrice::productId, ProductPrice::brandId)
        .containsExactly(productWithOnePrice, brandWithOnePrice);
  }

  @Test
  void returnEmptyWhenDateIsAfter() {
    Optional<ProductPrice> productPrice = productPriceRepository.findTheHighestRankedBy(
        new ProductPriceSearchCriteria(onlyPriceEndDate.plusSeconds(1), productWithOnePrice));

    assertThat(productPrice).isEmpty();
  }

  @Test
  void returnHighestRanked() {
    BrandId brandWithMultiplePrices = new BrandId("1");
    Optional<ProductPrice> productPrice = productPriceRepository.findTheHighestRankedBy(
        new ProductPriceSearchCriteria(highestRankedStartDate, productWithOnePrice));

    assertThat(productPrice).get()
        .extracting(ProductPrice::productId, ProductPrice::brandId, ProductPrice::startDate)
        .containsExactly(productWithOnePrice, brandWithMultiplePrices, highestRankedStartDate);
  }
}