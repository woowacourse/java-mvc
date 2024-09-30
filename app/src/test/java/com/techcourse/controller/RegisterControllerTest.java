package com.techcourse.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RegisterControllerTest {

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    @Test
    void register() {
        given()
                .contentType(ContentType.URLENC)
                .formParam("account", "mia")
                .formParam("password", "password")
                .formParam("email", "mia@gmail.com")
                .when()
                .post("/register")
                .then()
                .statusCode(302)
                .header("Location", containsString("/index.jsp"));
    }

    @Test
    void registerView() {
        given()
                .when()
                .get("/register/view")
                .then()
                .statusCode(200)
                .body(containsString("<title>회원가입</title>"));
    }
}
