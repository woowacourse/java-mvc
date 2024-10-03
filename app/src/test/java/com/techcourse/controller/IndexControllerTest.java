package com.techcourse.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IndexControllerTest {

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    @Test
    void indexView() {
        given()
                .when()
                .get("/")
                .then()
                .statusCode(200)
                .body(containsString("<title>대시보드</title>"));
    }
}
