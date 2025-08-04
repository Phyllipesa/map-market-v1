package com.albuquerque.map_market_v1.integrationTests.endtoend.exceptions;

import com.albuquerque.map_market_v1.config.TestConfig;
import com.albuquerque.map_market_v1.controllers.dto.input.RequestProductDto;
import com.albuquerque.map_market_v1.exception.messages.ProductErrorMessages;
import com.albuquerque.map_market_v1.integrationTests.testContainers.BaseAuthenticationE2ETest;

import org.junit.jupiter.api.*;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductExceptionIntegrationE2ETest extends BaseAuthenticationE2ETest {

    private static Long id;
    private static String actualMessage;
    private static String expectedMessage;
    private static RequestProductDto productRequest;

    @BeforeEach
    void resetRequestProductDto() {
        productRequest = new RequestProductDto();
    }

    @Test
    @Order(1)
    void shouldReturnNotFoundWhenProductDoesNotExistOnFindByIdWithIdInvalid() {
        // given
        id = 958L; // invalid id

        // when
        var response =
            given()
                .spec(specification)
                .basePath("/product")
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .pathParam("id", id)
                .when()
                .get("/{id}")
                .then()
                .statusCode(404)
                .extract()
                .response();

        // then
        actualMessage = response.jsonPath().getString("message");
        expectedMessage = ProductErrorMessages.PRODUCT_NOT_FOUND_WITH_ID + id;

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @Order(2)
    void shouldReturnNotFoundWhenProductDoesNotExistOnDeleteWithIdInvalid() {
        // given
        // when
        var response =
            given()
                .spec(specification)
                .basePath("/product")
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .pathParam("id", id) // invalid id
                .when()
                .delete("/{id}")
                .then()
                .statusCode(404)
                .extract()
                .response();

        // then
        actualMessage = response.jsonPath().getString("message");
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @Order(3)
    void shouldReturnNotFoundWhenProductDoesNotExistOnUpdateWithIdInvalid() {
        // given
        productRequest.setName("Castanha de Caju");
        productRequest.setPrice(new BigDecimal("14.86"));

        // when
        var response =
            given()
                .spec(specification)
                .basePath("/product")
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .body(productRequest)
                .pathParam("id", id) // invalid id
                .when()
                .put("{id}")
                .then()
                .statusCode(404)
                .extract()
                .response();

        // then
        actualMessage = response.jsonPath().getString("message");
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @Order(4)
    void shouldReturnBadRequestWhenNameIsNull() {
        // given
        id = 1L;
        productRequest.setPrice(new BigDecimal("14.86"));

        // when
        var response =
            given()
                .spec(specification)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .basePath("/product")
                .body(productRequest)
                .pathParam("id", id)
                .when()
                .put("{id}")
                .then()
                .statusCode(400)
                .extract()
                .response();

        // then
         actualMessage = response.jsonPath().getString("message");
         expectedMessage = ProductErrorMessages.requiredParameterMessage("name");

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @Order(5)
    void shouldReturnBadRequestWhenNameIsEmpty() {
        // given
        id = 1L;
        productRequest.setName("");
        productRequest.setPrice(new BigDecimal("14.86"));

        // when
        var response =
            given()
                .spec(specification)
                .basePath("/product")
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .body(productRequest)
                .pathParam("id", id)
                .when()
                .put("{id}")
                .then()
                .statusCode(400)
                .extract()
                .response();

        // then
        actualMessage = response.jsonPath().getString("message");

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @Order(6)
    void shouldReturnBadRequestWhenNameIsBlank() {
        // given
        id = 1L;
        productRequest.setName(" ");
        productRequest.setPrice(new BigDecimal("14.86"));

        // when
        var response =
            given()
                .spec(specification)
                .basePath("/product")
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .body(productRequest)
                .pathParam("id", id)
                .when()
                .put("{id}")
                .then()
                .statusCode(400)
                .extract()
                .response();

        // then
        actualMessage = response.jsonPath().getString("message");

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @Order(7)
    void shouldReturnBadRequestWhenPriceIsNull() {
        // given
        id = 1L;
        productRequest.setName("Castanha de Caju");
        productRequest.setPrice(null);

        // when
        var response =
            given()
                .spec(specification)
                .basePath("/product")
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .body(productRequest)
                .pathParam("id", id)
                .when()
                .put("{id}")
                .then()
                .statusCode(400)
                .extract()
                .response();

        // then
        actualMessage = response.jsonPath().getString("message");
        expectedMessage = ProductErrorMessages.requiredParameterMessage("price");

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @Order(8)
    void shouldReturnBadRequestWhenPriceIsNegative() {
        // given
        id = 1L;
        productRequest.setName("Castanha de Caju");
        productRequest.setPrice(new BigDecimal("-14.50"));

        // when
        var response =
            given()
                .spec(specification)
                .basePath("/product")
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .body(productRequest)
                .pathParam("id", id)
                .when()
                .put("{id}")
                .then()
                .statusCode(400)
                .extract()
                .response();

        // then
        actualMessage = response.jsonPath().getString("message");
        expectedMessage = ProductErrorMessages.NEGATIVE_NOT_ALLOWED;

        assertEquals(expectedMessage, actualMessage);
    }
}
