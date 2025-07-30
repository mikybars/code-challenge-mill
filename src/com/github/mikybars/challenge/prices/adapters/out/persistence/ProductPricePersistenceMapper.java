package com.github.mikybars.challenge.prices.adapters.out.persistence;

import com.github.mikybars.challenge.prices.domain.BrandId;
import com.github.mikybars.challenge.prices.domain.PriceListId;
import com.github.mikybars.challenge.prices.domain.ProductId;
import com.github.mikybars.challenge.prices.domain.ProductPrice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
interface ProductPricePersistenceMapper {

  @Mapping(target = "productId", source = "product.id")
  @Mapping(target = "brandId", source = "product.brand.id")
  @Mapping(target = "price", source = "entity")
  ProductPrice toDomain(ProductPriceEntity entity);

  default ProductId mapProductId(String id) {
    return new ProductId(id);
  }

  default BrandId mapBrandId(String id) {
    return new BrandId(id);
  }

  default PriceListId mapPriceListId(String id) {
    return new PriceListId(id);
  }
}
