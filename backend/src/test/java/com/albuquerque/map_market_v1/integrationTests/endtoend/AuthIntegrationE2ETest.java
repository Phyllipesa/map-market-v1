package com.albuquerque.map_market_v1.integrationTests.endtoend;

import com.albuquerque.map_market_v1.config.TestConfig;
import com.albuquerque.map_market_v1.entities.dtos.output.AccountCredentialsDto;
import com.albuquerque.map_market_v1.entities.dtos.output.TokenDto;
import com.albuquerque.map_market_v1.integrationTests.testContainers.AbstractIntegrationTest;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthIntegrationE2ETest extends AbstractIntegrationTest {

    private static TokenDto tokenDto;

    @Value("${test.user}")
    protected String username;

    @Value("${test.password}")
    protected String pass;

    @Test
    @Order(1)
    void shouldReturnAccessTokenWhenSignInEndpointCalledWithValidCredentials() {
        // given
        AccountCredentialsDto user = new AccountCredentialsDto(username, pass);

        // when
        tokenDto = given()
            .basePath("/auth/sign-in")
            .port(TestConfig.APPLICATION_PORT)
            .contentType(TestConfig.CONTENT_TYPE_JSON)
            .body(user)
            .when()
            .post()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .as(TokenDto.class);

        // then
        assertNotNull(tokenDto.getAccessToken());
        assertNotNull(tokenDto.getRefreshToken());
    }

    @Test
    @Order(2)
    void shouldReturnNewTokenWhenRefreshTokenEndpointCalledWithValidData() {
        //GIVEN
        //WHEN
        tokenDto = given()
            .basePath("/auth/refresh")
            .port(TestConfig.APPLICATION_PORT)
            .contentType(TestConfig.CONTENT_TYPE_JSON)
            .pathParam("username", username)
            .header(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDto.getRefreshToken())
            .when()
            .put("{username}")
            .then()
            .statusCode(200)
            .extract()
            .body()
            .as(TokenDto.class);

        //THEN
        assertNotNull(tokenDto.getAccessToken());
        assertNotNull(tokenDto.getRefreshToken());
    }
}
