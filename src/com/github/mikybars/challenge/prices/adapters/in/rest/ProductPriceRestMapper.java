package com.github.mikybars.challenge.prices.adapters.in.rest;

import com.github.mikybars.challenge.prices.application.ProductPriceSearchCriteria;
import com.github.mikybars.challenge.prices.domain.ProductId;
import com.github.mikybars.challenge.prices.domain.ProductPrice;
import java.time.LocalDateTime;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
interface ProductPriceRestMapper {

  @Mapping(target = "productId", source = "productId.id")
  @Mapping(target = "brandId", source = "brandId.id")
  @Mapping(target = "priceListId", source = "priceListId.id")
  @Mapping(target = "amount", source = "price.amount")
  @Mapping(target = "currencyCode", source = "price.currency")
  ProductPriceDto toResponseDto(ProductPrice productPrice);

  ProductPriceSearchCriteria toSearchCriteria(String productId, LocalDateTime applicationDate);

  ProductId toProductId(String id);
}
