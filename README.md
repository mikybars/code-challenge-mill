[![Build and Test](https://github.com/mikybars/code-challenge/actions/workflows/test.yml/badge.svg)](https://github.com/mikybars/code-challenge/actions/workflows/test.yml)
[![Coverage Status](https://coveralls.io/repos/github/mikybars/code-challenge/badge.svg?branch=main)](https://coveralls.io/github/mikybars/code-challenge?branch=main)

# Servicio de precios

## Descripción

Esta aplicación implementa un servicio para la gestión y consulta de precios de productos en un 
comercio electrónico expuesto a través de una API REST. Permite obtener el precio aplicable a un
producto específico según la fecha de consulta y la cadena comercial, teniendo en cuenta las
diferentes tarifas y prioridades configuradas en el sistema.

## Uso

### Requisitos previos

* JDK ≥ 21 (por ejemplo [Eclipse Temurin](https://adoptium.net/temurin/releases/?version=21))

### Ejecutar los tests

```bash
./gradlew test
```

### Probar la aplicación

Primero será necesario levantar la aplicación:
```bash
./gradlew bootRun --args='--spring.profiles.active=standalone'
```

La aplicación usará el puerto 8080 de forma predeterminada y tendrá los siguientes datos preconfigurados:

| BRAND ID | START DATE          | END DATE            | PRICE LIST | PRODUCT ID | RANK | AMOUNT | CURRENCY |
| -------- | ------------------- | ------------------- | ---------- | ---------- |------|--------|----------|
| 1        | 2020-06-14-00.00.00 | 2020-12-31-23.59.59 | 1          | 35455      | 0    | 35.50  | EUR      |
| 1        | 2020-06-14-15.00.00 | 2020-06-14-18.30.00 | 2          | 35455      | 1    | 25.45  | EUR      |
| 1        | 2020-06-15-00.00.00 | 2020-06-15-11.00.00 | 3          | 35455      | 1    | 30.50  | EUR      |
| 1        | 2020-06-15-16.00.00 | 2020-12-31-23.59.59 | 4          | 35455      | 1    | 38.95  | EUR      |

Para enviar una petición de consulta, el proyecto incluye una colección y un entorno de Postman.
Alternativamente se puede utilizar la siguiente orden:

```bash
curl --location 'http://localhost:8080/prices?applicationDate=2020-06-14T15%3A00%3A00Z&productId=35455&brandId=1'
```

## Stack tecnológico

### Principal
- Spring Boot 3.4.4
- Java 21
- Gradle 8.13
- Spring Web MVC
- Spring Data JPA

### Base de datos
- H2: Base de datos en memoria

### Herramientas de desarrollo
- MapStruct: Mapeo de objetos
- Lombok: Reducción de código boilerplate
- [OpenAPI Generator](https://github.com/OpenAPITools/openapi-generator): Generación de código a partir de especificaciones OpenAPI
- [Spring Boot starter para la gestión de errores mediante configuración](https://github.com/wimdeblauwe/error-handling-spring-boot-starter)

### Testing
- JUnit 5
- AssertJ
- Mockito
- [Spring Test Slices](https://docs.spring.io/spring-boot/appendix/test-auto-configuration/slices.html)
- [Approval Tests](https://www.youtube.com/watch?v=QEdpE0chA-s): verificación de tests simplificada para objetos complejos
- [ArchUnit](https://www.archunit.org/): tests de arquitectura y convenciones de programación

## Arquitectura

El proyecto implementa una [arquitectura hexagonal](https://alistair.cockburn.us/hexagonal-architecture/) (también conocida como _Ports and Adapters_)
que [se ve reflejada en su estructura](https://blog.cleancoder.com/uncle-bob/2011/09/30/Screaming-Architecture.html) y que es [verificada mediante el uso
de tests unitarios](https://www.archunit.org/).

Además sigue un enfoque API First, por el cual los componentes encargados de manejar las peticiones
REST son autogenerados a partir de una especificación previa.

## Testing

El proyecto cuenta con una batería completa de pruebas que incluye:

* Tests unitarios para verificar la correcta interacción entre los componentes del sistema con el
apoyo de mocks
* Tests de validación de contrato OpenAPI
* Tests de integración para las consultas de base de datos
* Tests E2E con el camino esperado
* Tests de validación de arquitectura y buenas prácticas de codificación

## API REST

```
GET /prices
```

Este endpoint permite consultar precios de productos según varios criterios.

### Parámetros requeridos

| Parámetro     | Tipo     | Descripción                                |
|---------------|----------|-------------------------------------------|
| productId     | Long     | Identificador del producto                |
| brandId       | Long     | Identificador de la cadena del grupo      |
| date          | String   | Fecha de aplicación (formato: yyyy-MM-dd-HH.mm.ss) |

### Ejemplo de uso

Petición:
```
GET /prices?productId=35455&brandId=1&date=2020-06-14-10.00.00
```

Respuesta:
```json
{
    "productId": 35455,
    "brandId": 1,
    "priceList": 1,
    "startDate": "2020-06-14-00.00.00",
    "endDate": "2020-12-31-23.59.59",
    "price": 35.50
}
```

### Códigos de respuesta

- 200: Éxito - Retorna el precio aplicable
- 400: Error de validación - Parámetros incorrectos
- 404: No encontrado - No existe precio para los criterios especificados
- 500: Error interno del servidor
