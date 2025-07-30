package com.github.mikybars.challenge.prices.application;

import com.github.mikybars.challenge.prices.domain.ProductId;
import java.time.LocalDateTime;

public record ProductPriceSearchCriteria(LocalDateTime applicationDate, ProductId productId) {

}