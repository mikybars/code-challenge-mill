package com.github.mikybars.challenge.prices.adapters.out.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;

@Entity
@Getter
@Table(name = "product_price")
class ProductPriceEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  long id;
  @ManyToOne
  ProductEntity product;
  String priceListId;
  LocalDateTime startDate;
  LocalDateTime endDate;
  String amount;
  String currency;
}
