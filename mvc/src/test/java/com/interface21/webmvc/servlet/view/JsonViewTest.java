package com.interface21.webmvc.servlet.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class JsonViewTest {

    private JsonView jsonView;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter stringWriter;

    @BeforeEach
    void setUp() throws IOException {
        jsonView = new JsonView();
        request = Mockito.mock(HttpServletRequest.class);
        response = Mockito.mock(HttpServletResponse.class);
        stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(printWriter);
    }

    @Test
    @DisplayName("model에 데이터가 1개면 값을 그대로 반환한다.")
    void render_size_1() throws Exception {
        Map<String, Object> model = Map.of("user", new User("pororo"));
        jsonView.render(model, request, response);

        assertThat(stringWriter.toString()).isEqualTo("{\"name\":\"pororo\"}");
    }

    @Test
    @DisplayName("model에 데이터가 2개 이상이면 Map 형태 그대로 JSON으로 변환해서 반환")
    void render_multi_values() throws Exception {
        Map<String, Object> model = Map.of("user", new User("pororo"), "admin", new User("admin"));
        jsonView.render(model, request, response);

        assertThat(stringWriter.toString()).contains(
                "\"user\":{\"name\":\"pororo\"",
                "\"admin\":{\"name\":\"admin\"");
    }

    private record User(String name) {
    }
}
