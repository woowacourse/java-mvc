package com.techcourse.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoginControllerTest {

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    @Test
    void login() {
        given()
                .contentType(ContentType.URLENC)
                .formParam("account", "gugu")
                .formParam("password", "password")
                .when()
                .post("/login")
                .then()
                .statusCode(302)
                .header("Location", containsString("/index.jsp"));
    }

    @Test
    void loginView() {
        given()
                .when()
                .get("/login/view")
                .then()
                .statusCode(200)
                .body(containsString("<title>로그인</title>"));
    }
}
