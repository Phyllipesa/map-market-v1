package com.albuquerque.map_market_v1.integrationTests.testContainers;

import com.albuquerque.map_market_v1.config.TestConfig;
import com.albuquerque.map_market_v1.entities.dtos.output.AccountCredentialsDto;
import com.albuquerque.map_market_v1.entities.dtos.output.TokenDto;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;

import static io.restassured.RestAssured.given;

@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseAuthenticationE2ETest extends AbstractIntegrationTest {

    protected RequestSpecification specification;

    @Value("${test.user}")
    protected String username;

    @Value("${test.password}")
    protected String pass;

    @BeforeAll
    void authenticate() {

        AccountCredentialsDto user = new AccountCredentialsDto(username, pass);

        var accessToken =
            given()
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
                .as(TokenDto.class)
                .getAccessToken();

        specification = new RequestSpecBuilder()
            .addHeader(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
            .setPort(TestConfig.APPLICATION_PORT)
            .addFilter(new RequestLoggingFilter(LogDetail.ALL))
            .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
            .build();
    }
}
