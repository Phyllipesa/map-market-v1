package com.albuquerque.map_market_v1.integrationTests.endtoend;

import com.albuquerque.map_market_v1.config.TestConfig;
import com.albuquerque.map_market_v1.controllers.dto.input.RequestProductDto;
import com.albuquerque.map_market_v1.entities.dtos.output.ProductDto;
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
class ProductIntegrationE2ETest extends BaseAuthenticationE2ETest {

    private static RequestProductDto productRequest;
    private static ProductDto productResponse;
    private static ObjectMapper objectMapper;

    @BeforeAll
    static void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        productRequest = new RequestProductDto();
        productRequest.setName("Castanha do Pará");
        productRequest.setPrice(new BigDecimal("35.99"));
    }

    @Test
    @Order(1)
    void shouldReturnResourceByIdWhenCallingFindByIdEndpoint() throws IOException {
        // given
        // when
        var content =
            given()
                .spec(specification)
                .basePath("/product")
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .pathParam("id", 73)
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        productResponse = objectMapper.readValue(content, ProductDto.class);

        // then
        assertNotNull(productResponse);
        assertNotNull(productResponse.getId());
        assertNotNull(productResponse.getName());
        assertNotNull(productResponse.getPrice());

        assertTrue(productResponse.getId() > 0);

        assertEquals("Cereal matinal integral", productResponse.getName());
        assertEquals(0, productResponse.getPrice().compareTo(new BigDecimal("6.00")));
    }

    @Test
    @Order(2)
    void shouldReturnAllResourcesWhenCallingFindAllEndpoint() throws IOException {
        // given
        // when
        var content =
            given()
                .spec(specification)
                .basePath("/product")
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .accept(TestConfig.CONTENT_TYPE_JSON)
                .when()
                .get()
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
    @Order(3)
    void shouldCreateResourceWhenCallingCreateEndpointWithValidData() throws IOException {
        // given
        // when
        var content =
            given()
                .spec(specification)
                .basePath("/product")
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .body(productRequest)
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract()
                .body()
                .asString();

        productResponse = objectMapper.readValue(content, ProductDto.class);

        // then
        assertNotNull(productResponse);
        assertNotNull(productResponse.getId());
        assertNotNull(productResponse.getName());
        assertNotNull(productResponse.getPrice());

        assertTrue(productResponse.getId() > 0);

        assertEquals(130L, productResponse.getId());
        assertEquals("Castanha do Pará", productResponse.getName());
        assertEquals(0, productResponse.getPrice().compareTo(new BigDecimal("35.99")));
    }

    @Test
    @Order(4)
    void shouldUpdateResourceWhenCallingUpdateEndpointWithValidData() throws IOException {
        // given
        productRequest.setName("Castanha de Caju");
        productRequest.setPrice(new BigDecimal("14.86"));

        // when
        var content =
            given()
                .spec(specification)
                .basePath("/product")
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .body(productRequest)
                .pathParam("id", 130)
                .when()
                .put("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        productResponse = objectMapper.readValue(content, ProductDto.class);

        // then
        assertNotNull(productResponse);
        assertNotNull(productResponse.getId());
        assertNotNull(productResponse.getName());
        assertNotNull(productResponse.getPrice());

        assertTrue(productResponse.getId() > 0);

        assertEquals("Castanha de Caju", productResponse.getName());
        assertEquals(0, productResponse.getPrice().compareTo(new BigDecimal("14.86")));
    }

    @Test
    @Order(5)
    void shouldDeleteResourceWhenCallingDeleteEndpointWithExistingId() {
        // given
        // when
        // then
        given()
            .spec(specification)
            .basePath("/product")
            .contentType(TestConfig.CONTENT_TYPE_JSON)
            .pathParam("id", 130)
            .when()
            .delete("{id}")
            .then()
            .statusCode(204);
    }
}
