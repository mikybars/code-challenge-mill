package com.github.mikybars.challenge.prices.adapters.out.persistence;

import com.github.mikybars.challenge.prices.application.ProductPriceSearchCriteria;
import com.github.mikybars.challenge.prices.application.ports.out.ProductPriceRepository;
import com.github.mikybars.challenge.prices.domain.ProductPrice;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class InMemoryProductPriceRepository implements ProductPriceRepository {

  private final JpaRepository jpaRepository;
  private final ProductPricePersistenceMapper persistenceMapper;

  @Override
  public Optional<ProductPrice> findTheHighestRankedBy(ProductPriceSearchCriteria searchCriteria) {
    return jpaRepository
        .findTheHighestRankedBy(searchCriteria)
        .map(persistenceMapper::toDomain);
  }

  interface JpaRepository extends
      org.springframework.data.jpa.repository.JpaRepository<ProductPriceEntity, Long> {

    @Query(value = """
        SELECT * FROM product_price
        WHERE product_id = :#{#criteria.productId.id}
          AND start_date <= :#{#criteria.applicationDate}
          AND end_date >= :#{#criteria.applicationDate}
        ORDER BY rank DESC
        LIMIT 1
        """,
        nativeQuery = true)
    Optional<ProductPriceEntity> findTheHighestRankedBy(ProductPriceSearchCriteria criteria);
  }
}
