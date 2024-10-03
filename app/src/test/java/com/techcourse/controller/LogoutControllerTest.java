package com.techcourse.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LogoutControllerTest {

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    @Test
    void logout() {
        given()
                .when()
                .post("/logout")
                .then()
                .statusCode(302)
                .header("Location", containsString("/"));
    }
}
