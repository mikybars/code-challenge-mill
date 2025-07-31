package com.github.mikybars.challenge.prices.adapters.in.rest;

import static org.mockito.Mockito.when;

import com.github.mikybars.challenge.common.NotFoundException;
import com.github.mikybars.challenge.prices.ProductPrices;
import com.github.mikybars.challenge.prices.application.ProductPriceSearchCriteria;
import com.github.mikybars.challenge.prices.application.ports.in.GetProductPriceUseCase;
import com.github.mikybars.challenge.prices.domain.ProductId;
import java.time.LocalDateTime;
import org.approvaltests.JsonApprovals;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;

@WebMvcTest
@Import(ProductPriceRestMapperImpl.class)
class GetProductPriceRestTest {

  @Autowired
  WebTestClient webClient;

  @MockitoBean
  GetProductPriceUseCase getProductPriceUseCase;

  @Test
  void getProductPrice() {
    when(getProductPriceUseCase.execute(
        new ProductPriceSearchCriteria(sometime(), ProductPrices.any().productId()))
    ).thenReturn(ProductPrices.any());

    ResponseSpec response = webClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/prices")
            .queryParam("applicationDate", sometime().toString())
            .queryParam("productId", ProductPrices.any().productId().id())
            .build())
        .accept(MediaType.APPLICATION_JSON)
        .exchange();

    response
        .expectStatus().isOk()
        .expectBody(String.class).consumeWith(result ->
            JsonApprovals.verifyJson(result.getResponseBody()));
  }

  @Test
  void doesNotGetProductPrice() {
    when(getProductPriceUseCase.execute(
        new ProductPriceSearchCriteria(sometime(), productNotFoundId()))
    ).thenThrow(new NotFoundException("no price found for input params"));

    ResponseSpec response = webClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/prices")
            .queryParam("applicationDate", sometime().toString())
            .queryParam("productId", productNotFoundId().id())
            .build())
        .accept(MediaType.APPLICATION_JSON)
        .exchange();

    response
        .expectStatus().isNotFound()
        .expectBody(String.class).consumeWith(result ->
            JsonApprovals.verifyJson(result.getResponseBody()));
  }

  @Test
  void validationErrorsMeetStandards() {
    ResponseSpec response = webClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/prices")
            // .queryParam("applicationDate", "is missing")
            .build())
        .accept(MediaType.APPLICATION_JSON)
        .exchange();

    response
        .expectStatus().isBadRequest()
        .expectBody()
        .jsonPath("code").exists()
        .jsonPath("message").exists();
  }

  static LocalDateTime sometime() {
    return LocalDateTime.parse("2020-06-14T00:00:00");
  }

  static ProductId productNotFoundId() {
    return new ProductId("35456");
  }
}