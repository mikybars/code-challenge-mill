package com.github.mikybars.challenge.prices.domain;

import java.time.LocalDateTime;

public record ProductPrice(ProductId productId, BrandId brandId, PriceListId priceListId,
                           LocalDateTime startDate, LocalDateTime endDate,
                           Money price) {

}
