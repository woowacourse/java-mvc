import static org.assertj.core.api.Assertions.assertThat;

import com.techcourse.TomcatStarter;
import io.restassured.RestAssured;
import io.restassured.http.Cookie;
import io.restassured.http.Cookies;
import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import web.org.springframework.http.MediaType;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class IntegrationTest {

    private static Thread thread;
    private static TomcatStarter tomcatStarter;

    @BeforeAll
    static void beforeAll() {
        Runnable runnable = () -> {
            try {
                tomcatStarter = new TomcatStarter("src/main/webapp/", 8080);
                tomcatStarter.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        thread = new Thread(runnable);
        thread.start();
    }

    @AfterAll
    static void afterAll() {
        tomcatStarter.stop();
        thread.interrupt();
    }

    @ParameterizedTest
    @ValueSource(strings = {"/", "/login/view", "/register/view"})
    void 페이지를_반환할_수_있다(String path)  {
        // given, when
        RestAssured.when()
                .get(path)
                // then
                .then()
                .statusCode(200);
    }

    @Test
    void 로그인을_할_수_있다() {
        // given, when
        RestAssured.given()
                .contentType(ContentType.APPLICATION_FORM_URLENCODED.getMimeType())
                .param("account", "gugu")
                .param("password", "password")
                .post("/login")
                // then
                .then()
                .statusCode(302);
    }

    @Test
    void 회원가입을_할_수_있다() {
        // given, when
        RestAssured.given()
                .contentType(ContentType.APPLICATION_FORM_URLENCODED.getMimeType())
                .param("account", "gugu2")
                .param("password", "password2")
                .post("/register")
                // then
                .then()
                .statusCode(302);
    }

    @Test
    void 로그아웃을_할_수_있다() {
        // given, when
        RestAssured.given()
                .contentType(ContentType.APPLICATION_FORM_URLENCODED.getMimeType())
                .cookie("JSESSIONID", "test")
                .get("/logout")
                // then
                .then()
                .statusCode(200);
    }
}
