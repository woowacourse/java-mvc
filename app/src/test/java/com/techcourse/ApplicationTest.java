package com.techcourse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import org.apache.catalina.LifecycleException;

import static org.assertj.core.api.Assertions.assertThat;

class ApplicationTest {

    private static final String PORT = "8081";
    private static final String RENDER_FORMAT = "%s rendered";

    private ByteArrayOutputStream captor;

    @BeforeEach
    void setUp() throws Exception {
        captor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(captor));
        JwpApplication.main(new String[]{PORT});
    }

    @AfterEach
    void tearDown() throws LifecycleException {
        JwpApplication.stopTomcat();
    }

    @ParameterizedTest
    @MethodSource
    void 서블릿_JSP_응답_테스트(String path, String resource) throws Exception {

        // when
        final HttpURLConnection connection = connectTomcat(path);

        // then
        assertThat(connection.getHeaderField(null)).isEqualTo("HTTP/1.1 200");
        assertThat(captor.toString().trim()).contains(String.format(RENDER_FORMAT, resource));
    }

    private static Stream<Arguments> 서블릿_JSP_응답_테스트() {
        return Stream.of(
                Arguments.of("/", "index.jsp"),
                Arguments.of("/login/view", "login.jsp"),
                Arguments.of("/register/view", "register.jsp")
        );
    }

    private HttpURLConnection connectTomcat(String path) throws IOException {
        final URL url = new URL("http://localhost:" + PORT + path);
        return (HttpURLConnection) url.openConnection();
    }

    @ParameterizedTest
    @MethodSource
    void 서블릿_JSON_응답_테스트(String path, String response) throws Exception {

        // when
        final HttpURLConnection connection = connectTomcat(path);

        // then
        assertThat(connection.getHeaderField(null)).isEqualTo("HTTP/1.1 200");
    }

    private static Stream<Arguments> 서블릿_JSON_응답_테스트() {
        return Stream.of(
                Arguments.of("/api/user?account=gugu", "User{id=1, account='gugu', email='hkkang@woowahan.com', password='password'}")
        );
    }
}
