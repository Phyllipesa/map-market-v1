package com.albuquerque.map_market_v1.integrationTests.endtoend;

import com.albuquerque.map_market_v1.config.TestConfig;
import com.albuquerque.map_market_v1.entities.dtos.output.LocationDto;
import com.albuquerque.map_market_v1.entities.dtos.output.ProductDto;
import com.albuquerque.map_market_v1.entities.dtos.output.ShelvingUnitDto;
import com.albuquerque.map_market_v1.integrationTests.testContainers.AbstractIntegrationTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MapMarketIntegrationE2ETest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static LocationDto locationDto;
    private static ProductDto productResponse;
    private static ShelvingUnitDto shelvingResponse;
    private static ObjectMapper objectMapper;

    @BeforeAll
    static void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        specification = new RequestSpecBuilder()
            .setBasePath("map")
            .setPort(TestConfig.APPLICATION_PORT)
            .build();
    }

    @Test
    void shouldReturnAllResourcesWhenCallingFindRegisteredProductsEndpoint() throws IOException {
        // given
        // when
        var content =
            given()
                .spec(specification)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .accept(TestConfig.CONTENT_TYPE_JSON)
                .when()
                .get("/products")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        List<ProductDto> response = objectMapper.readValue(content, new TypeReference<>() {});

        // then
        assertNotNull(response);

        productResponse = response.getFirst();
        assertNotNull(productResponse);
        assertNotNull(productResponse.getId());
        assertNotNull(productResponse.getName());
        assertNotNull(productResponse.getPrice());

        assertEquals(1L, productResponse.getId());
        assertEquals("Arroz branco", productResponse.getName());
        assertEquals(0, productResponse.getPrice().compareTo(new BigDecimal("4.50")));


        productResponse = response.get(2);
        assertNotNull(productResponse);
        assertNotNull(productResponse.getId());
        assertNotNull(productResponse.getName());
        assertNotNull(productResponse.getPrice());

        assertEquals(3L, productResponse.getId());
        assertEquals("Arroz parboilizado", productResponse.getName());
        assertEquals(0, productResponse.getPrice().compareTo(new BigDecimal("5.50")));
    }

    @Test
    void shouldReturnAllResourcesWhenCallingFindByProductIsNotNullEndpoint() throws IOException {
        // given
        // when
        var content =
            given()
                .spec(specification)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .accept(TestConfig.CONTENT_TYPE_JSON)
                .when()
                .get("/locations")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        List<LocationDto> response = objectMapper.readValue(content, new TypeReference<>() {});

        // then
        assertNotNull(response);

        locationDto = response.getFirst();
        assertAll(
            () -> assertNotNull(locationDto),
            () -> assertEquals(1L, locationDto.getId()),
            () -> assertEquals(1L, locationDto.getShelvingUnitId()),
            () -> assertEquals("A", locationDto.getSide()),
            () -> assertEquals(1, locationDto.getPart()),
            () -> assertEquals(1, locationDto.getShelf()),
            () -> assertNotNull(locationDto.getProduct()),
            () -> assertEquals(1, locationDto.getProduct().getId()),
            () -> assertEquals("Arroz branco", locationDto.getProduct().getName()),
            () -> assertEquals(0, locationDto.getProduct().getPrice().compareTo(
                new BigDecimal("4.50"))
            )
        );

        locationDto = response.get(125);
        assertAll(
            () -> assertNotNull(locationDto),
            () -> assertEquals(126L, locationDto.getId()),
            () -> assertEquals(4L, locationDto.getShelvingUnitId()),
            () -> assertEquals("B", locationDto.getSide()),
            () -> assertEquals(4, locationDto.getPart()),
            () -> assertEquals(2, locationDto.getShelf()),
            () -> assertNotNull(locationDto.getProduct()),
            () -> assertEquals(126, locationDto.getProduct().getId()),
            () -> assertEquals("Balde", locationDto.getProduct().getName()),
            () -> assertEquals(0, locationDto.getProduct().getPrice().compareTo(
                new BigDecimal("12.00"))
            )
        );
    }

    @Test
    void shouldReturnResourceByProductIdWhenCallingFindLocationByProductIdEndpoint() throws IOException {
        // given
        // when
        var content =
            given()
                .spec(specification)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .pathParam("id", 8)
                .when()
                .get("/location/product/{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        locationDto = objectMapper.readValue(content, LocationDto.class);

        // then
        assertAll(
            () -> assertNotNull(locationDto),
            () -> assertEquals(8L, locationDto.getId()),
            () -> assertEquals(1L, locationDto.getShelvingUnitId()),
            () -> assertEquals("A", locationDto.getSide()),
            () -> assertEquals(2, locationDto.getPart()),
            () -> assertEquals(4, locationDto.getShelf()),
            () -> assertNotNull(locationDto.getProduct()),
            () -> assertEquals(8, locationDto.getProduct().getId()),
            () -> assertEquals("Lentilha", locationDto.getProduct().getName()),
            () -> assertEquals(0, locationDto.getProduct().getPrice().compareTo(
                new BigDecimal("9.00"))
            )
        );
    }

    @Test
    void shouldReturnAllResourcesWhenCallingFindShelvingUnitsWithProductEndpoint() throws JsonProcessingException {
        // given
        // when
        var content =
            given()
                .spec(specification)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .accept(TestConfig.CONTENT_TYPE_JSON)
                .when()
                .get("/shelvings")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        List<ShelvingUnitDto> response = objectMapper.readValue(content, new TypeReference<>() {});

        // then
        assertNotNull(response);

        shelvingResponse = response.getFirst();
        assertAll(
            () -> assertNotNull(shelvingResponse),
            () -> assertEquals(1L, shelvingResponse.getId()),
            () -> assertEquals(1L, shelvingResponse.getUnit()),
            () -> assertEquals("Grãos", shelvingResponse.getSideA()),
            () -> assertEquals("Farinhas", shelvingResponse.getSideB())
        );

        shelvingResponse = response.getLast();
        assertAll(
            () -> assertNotNull(shelvingResponse),
            () -> assertEquals(4L, shelvingResponse.getId()),
            () -> assertEquals(4L, shelvingResponse.getUnit()),
            () -> assertEquals("Higiene Pessoal", shelvingResponse.getSideA()),
            () -> assertEquals("Produtos de Limpeza", shelvingResponse.getSideB())
        );
    }
}
