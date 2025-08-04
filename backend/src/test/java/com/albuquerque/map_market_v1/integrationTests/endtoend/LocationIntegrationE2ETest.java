package com.albuquerque.map_market_v1.integrationTests.endtoend;

import com.albuquerque.map_market_v1.config.TestConfig;
import com.albuquerque.map_market_v1.entities.dtos.output.LocationDto;

import com.albuquerque.map_market_v1.integrationTests.testContainers.BaseAuthenticationE2ETest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.*;

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
class LocationIntegrationE2ETest extends BaseAuthenticationE2ETest {

    private static LocationDto locationDto;
    private static ObjectMapper objectMapper;

    @BeforeAll
    static void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    @Test
    void shouldReturnResourceByIdWhenCallingFindByIdEndpoint() throws IOException {
        // given
        // when
        var content =
            given()
                .spec(specification)
                .basePath("/location")
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .pathParam("id", 7)
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        locationDto = objectMapper.readValue(content, LocationDto.class);

        // then
        assertAll(
            () -> assertNotNull(locationDto),
            () -> assertEquals(7L, locationDto.getId()),
            () -> assertEquals(1L, locationDto.getShelvingUnitId()),
            () -> assertEquals("A", locationDto.getSide()),
            () -> assertEquals(2, locationDto.getPart()),
            () -> assertEquals(3, locationDto.getShelf()),
            () -> assertNotNull(locationDto.getProduct()),
            () -> assertEquals(7, locationDto.getProduct().getId()),
            () -> assertEquals("Feijão branco", locationDto.getProduct().getName()),
            () -> assertEquals(0, locationDto.getProduct().getPrice().compareTo(
                new BigDecimal("8.00"))
            )
        );
    }

    @Test
    void shouldReturnAllResourcesWhenCallingFindAllEndpoint() throws IOException {
        // given
        // when
        var content =
            given()
                .spec(specification)
                .basePath("/location")
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .accept(TestConfig.CONTENT_TYPE_JSON)
                .when()
                .get()
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
    void shouldRegisterProductInLocationWhenCallingSubscribingProductEndpoint() throws IOException {
        // given
        // when
        var content =
            given()
                .spec(specification)
                .basePath("/location")
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .pathParam("locationId", 127)
                .pathParam("productId", 127)
                .when()
                .put("/{locationId}/product/{productId}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        locationDto = objectMapper.readValue(content, LocationDto.class);

        // then
        assertAll(
            () -> assertNotNull(locationDto),
            () -> assertEquals(127L, locationDto.getId()),
            () -> assertEquals(4L, locationDto.getShelvingUnitId()),
            () -> assertEquals("B", locationDto.getSide()),
            () -> assertEquals(4, locationDto.getPart()),
            () -> assertEquals(3, locationDto.getShelf()),
            () -> assertNotNull(locationDto.getProduct()),
            () -> assertEquals(127L, locationDto.getProduct().getId()),
            () -> assertEquals("Saco de lixo", locationDto.getProduct().getName()),
            () -> assertEquals(0, locationDto.getProduct().getPrice().compareTo(
                new BigDecimal("4.00"))
            )
        );
    }

    @Test
    void shouldUnregisterProductFromLocationWhenCallingUnsubscribingProductEndpoint() throws IOException {
        // given
        // when
        var content =
            given()
                .spec(specification)
                .basePath("/location")
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .pathParam("id", 125)
                .when()
                .put("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        locationDto = objectMapper.readValue(content, LocationDto.class);

        // then
        assertAll(
            () -> assertNotNull(locationDto),
            () -> assertEquals(125L, locationDto.getId()),
            () -> assertEquals(4L, locationDto.getShelvingUnitId()),
            () -> assertEquals("B", locationDto.getSide()),
            () -> assertEquals(4, locationDto.getPart()),
            () -> assertEquals(1, locationDto.getShelf()),
            () -> assertNull(locationDto.getProduct())
        );
    }
}
