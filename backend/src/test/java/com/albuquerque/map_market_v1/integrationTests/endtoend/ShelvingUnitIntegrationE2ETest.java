package com.albuquerque.map_market_v1.integrationTests.endtoend;

import com.albuquerque.map_market_v1.config.TestConfig;
import com.albuquerque.map_market_v1.controllers.dto.input.RequestPatchShelvingUnitDto;
import com.albuquerque.map_market_v1.controllers.dto.input.RequestShelvingUnitDto;
import com.albuquerque.map_market_v1.entities.dtos.output.ShelvingUnitDto;
import com.albuquerque.map_market_v1.integrationTests.testContainers.BaseAuthenticationE2ETest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.*;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ShelvingUnitIntegrationE2ETest extends BaseAuthenticationE2ETest {

    private static RequestShelvingUnitDto request;
    private static ShelvingUnitDto shelvingUniResponse;
    private static ObjectMapper objectMapper;

    @BeforeAll
    static void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        request = new RequestShelvingUnitDto();
        request.setUnit(5L);
        request.setSideA("Hortifruti");
    }

    @Test
    @Order(1)
    void shouldReturnResourceByIdWhenCallingFindByIdEndpoint() throws IOException {
        // given
        // when
        var content =
            given()
                .spec(specification)
                .basePath("/shelving-unit")
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .pathParam("id", 1)
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        shelvingUniResponse = objectMapper.readValue(content, ShelvingUnitDto.class);

        // then
        assertAll(
            () -> assertNotNull(shelvingUniResponse),
            () -> assertEquals(1L, shelvingUniResponse.getId()),
            () -> assertEquals(1L, shelvingUniResponse.getUnit()),
            () -> assertEquals("Grãos", shelvingUniResponse.getSideA()),
            () -> assertEquals("Farinhas", shelvingUniResponse.getSideB())
        );
    }

    @Test
    @Order(2)
    void shouldReturnAllResourcesWhenCallingFindAllEndpoint() throws IOException {
        // given
        // when
        var content =
            given()
                .spec(specification)
                .basePath("/shelving-unit")
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .accept(TestConfig.CONTENT_TYPE_JSON)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        List<ShelvingUnitDto> response = objectMapper.readValue(content, new TypeReference<>() {});

        // then
        assertNotNull(response);

        shelvingUniResponse = response.getFirst();
        assertAll(
            () -> assertNotNull(shelvingUniResponse),
            () -> assertEquals(1L, shelvingUniResponse.getId()),
            () -> assertEquals(1L, shelvingUniResponse.getUnit()),
            () -> assertEquals("Grãos", shelvingUniResponse.getSideA()),
            () -> assertEquals("Farinhas", shelvingUniResponse.getSideB())
        );

        shelvingUniResponse = response.getLast();
        assertAll(
            () -> assertNotNull(shelvingUniResponse),
            () -> assertEquals(4L, shelvingUniResponse.getId()),
            () -> assertEquals(4L, shelvingUniResponse.getUnit()),
            () -> assertEquals("Higiene Pessoal", shelvingUniResponse.getSideA()),
            () -> assertEquals("Produtos de Limpeza", shelvingUniResponse.getSideB())
        );
    }

    @Test
    @Order(3)
    void shouldCreateResourceWhenCallingCreateEndpointWithValidData() throws IOException {
        // given
        // when
        var content =
            given()
                .spec(specification)
                .basePath("/shelving-unit")
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .body(request)
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract()
                .body()
                .asString();

        shelvingUniResponse = objectMapper.readValue(content, ShelvingUnitDto.class);

        // then
        assertAll(
            () -> assertNotNull(shelvingUniResponse),
            () -> assertEquals(5L, shelvingUniResponse.getId()),
            () -> assertEquals(5L, shelvingUniResponse.getUnit()),
            () -> assertEquals("Hortifruti", shelvingUniResponse.getSideA()),
            () -> assertNull(shelvingUniResponse.getSideB())
        );
    }

    @Test
    @Order(4)
    void shouldUpdateResourceWhenCallingUpdateEndpointWithValidData() throws IOException {
        // given
        request.setUnit(3L);
        request.setSideA("Padaria");
        request.setSideB("Bebidas");

        // when
        var content =
            given()
                .spec(specification)
                .basePath("/shelving-unit")
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .body(request)
                .pathParam("id", 3)
                .when()
                .put("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        shelvingUniResponse = objectMapper.readValue(content, ShelvingUnitDto.class);

        // then
        assertAll(
            () -> assertNotNull(shelvingUniResponse),
            () -> assertEquals(3L, shelvingUniResponse.getId()),
            () -> assertEquals(3L, shelvingUniResponse.getUnit()),
            () -> assertEquals("Padaria", shelvingUniResponse.getSideA()),
            () -> assertEquals("Bebidas", shelvingUniResponse.getSideB())
        );
    }

    @Test
    @Order(5)
    void shouldUpdateResourcePartiallyWhenCallingPatchEndpoint() throws IOException {
        // given
        RequestPatchShelvingUnitDto requestPatch = new RequestPatchShelvingUnitDto("Frios");

        // when
        var content =
            given()
                .spec(specification)
                .basePath("/shelving-unit")
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .body(requestPatch)
                .pathParam("id", 5)
                .when()
                .patch("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        shelvingUniResponse = objectMapper.readValue(content, ShelvingUnitDto.class);

        // then
        assertAll(
            () -> assertNotNull(shelvingUniResponse),
            () -> assertEquals(5L, shelvingUniResponse.getId()),
            () -> assertEquals(5L, shelvingUniResponse.getUnit()),
            () -> assertEquals("Hortifruti", shelvingUniResponse.getSideA()),
            () -> assertEquals("Frios", shelvingUniResponse.getSideB())
        );
    }

    @Test
    @Order(6)
    void shouldDeleteResourceWhenCallingDeleteEndpointWithExistingId() {
        // given
        // when
        // then
        given()
            .spec(specification)
            .basePath("/shelving-unit")
            .contentType(TestConfig.CONTENT_TYPE_JSON)
            .pathParam("id", 5)
            .when()
            .delete("{id}")
            .then()
            .statusCode(204);
    }
}
