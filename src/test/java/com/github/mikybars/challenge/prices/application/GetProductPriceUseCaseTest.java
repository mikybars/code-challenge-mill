package com.github.mikybars.challenge.prices.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.github.mikybars.challenge.common.NotFoundException;
import com.github.mikybars.challenge.prices.ProductPrices;
import com.github.mikybars.challenge.prices.application.ports.in.GetProductPriceUseCase;
import com.github.mikybars.challenge.prices.application.ports.out.ProductPriceRepository;
import com.github.mikybars.challenge.prices.domain.ProductId;
import com.github.mikybars.challenge.prices.domain.ProductPrice;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetProductPriceUseCaseTest {

  GetProductPriceUseCase getProductPriceUseCase;

  @Mock
  ProductPriceRepository productPriceRepository;

  @BeforeEach
  void setUp() {
    getProductPriceUseCase = new ProductPriceService(productPriceRepository);
  }

  @Test
  void returnTheProductPrice() {
    var expectedProductPrice = ProductPrices.any();
    when(productPriceRepository.findTheHighestRankedBy(
        new ProductPriceSearchCriteria(sometime(), someProduct()))
    ).thenReturn(Optional.of(expectedProductPrice));

    ProductPrice productPrice =
        getProductPriceUseCase.execute(
            new ProductPriceSearchCriteria(sometime(), someProduct()));

    assertThat(productPrice).isEqualTo(expectedProductPrice);
  }

  @Test
  void throwWhenNoResults() {
    when(productPriceRepository.findTheHighestRankedBy(
        new ProductPriceSearchCriteria(sometime(), someProduct()))
    ).thenReturn(Optional.empty());

    assertThatThrownBy(
        () -> getProductPriceUseCase.execute(
            new ProductPriceSearchCriteria(sometime(), someProduct()))
    )
        .isInstanceOf(NotFoundException.class)
        .hasMessageContainingAll(sometime().toString(), someProduct().id());
  }

  static LocalDateTime sometime() {
    return LocalDateTime.parse("2020-06-14T00:00:00");
  }

  static ProductId someProduct() {
    return new ProductId("35456");
  }
}