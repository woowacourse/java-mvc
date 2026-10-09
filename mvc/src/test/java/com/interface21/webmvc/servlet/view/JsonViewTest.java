package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JsonViewTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JsonView jsonView = new JsonView();

    @Test
    @DisplayName("Model 값이 하나라면, 그 객체를 그대로 Json으로 파싱한다")
    void renderSingleModelValueAsJsonObject() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var output = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(output));
        final var user = new User(1L, "홍길동");

        jsonView.render(Map.of("user", user), request, response);

        assertThat(objectMapper.readTree(output.toString()))
                .isEqualTo(objectMapper.readTree("""
                        {"id": 1, "name": "홍길동"}
                        """));
    }

    @Test
    @DisplayName("Model 값이 여러개라면, Map 형태 그대로 Json으로 파싱한다")
    void renderMultipleModelValuesAsJsonMap() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var output = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(output));
        final var model = Map.of(
                "user", new User(1L, "홍길동"),
                "authenticated", true
        );

        jsonView.render(model, request, response);

        assertThat(objectMapper.readTree(output.toString()))
                .isEqualTo(objectMapper.readTree("""
                        {
                          "user": {"id": 1, "name": "홍길동"},
                          "authenticated": true
                        }
                        """));
    }

    @Test
    @DisplayName("Content-Type을 'application/json;charset=UTF-8'로 지정한다")
    void useApplicationJsonUtf8ContentType() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));

        jsonView.render(Map.of(), request, response);

        final var order = inOrder(response);
        order.verify(response).setContentType("application/json;charset=UTF-8");
        order.verify(response).getWriter();
    }

    public record User(Long id, String name) {
    }
}
