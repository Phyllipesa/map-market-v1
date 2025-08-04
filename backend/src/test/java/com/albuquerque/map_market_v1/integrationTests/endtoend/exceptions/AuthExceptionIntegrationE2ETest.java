package com.albuquerque.map_market_v1.integrationTests.endtoend.exceptions;

import com.albuquerque.map_market_v1.config.TestConfig;
import com.albuquerque.map_market_v1.entities.dtos.output.AccountCredentialsDto;
import com.albuquerque.map_market_v1.exception.messages.AuthErrorMessages;
import com.albuquerque.map_market_v1.integrationTests.testContainers.AbstractIntegrationTest;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthExceptionIntegrationE2ETest extends AbstractIntegrationTest {

    private static String actualMessage;
    private static String expectedMessage;

    @Test
    @Order(1)
    void shouldReturnUnauthorizedWhenSignInEndpointCalledWithInvalidCredentials() {
        // given
        String username = "test";
        String pass = "test";
        AccountCredentialsDto user = new AccountCredentialsDto(username, pass);

        // when
        var response =
            given()
                .basePath("/auth/sign-in")
                .port(TestConfig.APPLICATION_PORT)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .body(user)
                .when()
                .post()
                .then()
                .statusCode(401)
                .extract()
                .response();

        // then
        actualMessage = response.jsonPath().getString("message");
        expectedMessage = AuthErrorMessages.INVALID_USER_OR_PASSWORD;

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @Order(2)
    void shouldReturnUnauthorizedWhenRefreshTokenEndpointCalledWithNonexistentUser() {
        // given
        String username = "test";
        String pass = "test";
        AccountCredentialsDto user = new AccountCredentialsDto(username, pass);

        // when
        var response =
            given()
                .basePath("/auth/refresh")
                .port(TestConfig.APPLICATION_PORT)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .pathParam("username", "testInvalidUser")
                .header(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer ")
                .when()
                .put("{username}")
                .then()
                .statusCode(401)
                .extract()
                .response();

        // then
        actualMessage = response.jsonPath().getString("message");
        expectedMessage = AuthErrorMessages.USERNAME_NOT_FOUND + "testInvalidUser";

        assertEquals(expectedMessage, actualMessage);
    }
}
