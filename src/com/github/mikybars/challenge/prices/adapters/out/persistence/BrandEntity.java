package com.github.mikybars.challenge.prices.adapters.out.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;


@Entity
@Getter
@Table(name = "brand")
class BrandEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  long id;
  String name;
}
