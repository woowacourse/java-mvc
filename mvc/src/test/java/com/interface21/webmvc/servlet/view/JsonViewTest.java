package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.interface21.web.http.MediaType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JsonViewTest {
    private final JsonView jsonView = new JsonView();

    @DisplayName("model에 데이터가 1개인 경우, 값만 JSON으로 응답한다.")
    @Test
    void renderSingleValue() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        final Map<String, Object> model = Map.of("user", Map.of("account", "gugu"));

        jsonView.render(model, request, response);

        assertThat(stringWriter.toString()).isEqualTo("{\"account\":\"gugu\"}");
    }

    @DisplayName("model에 데이터가 2개 이상인 경우, Map 형태 그대로 응답한다.")
    @Test
    void renderMultipleValues() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        final Map<String, Object> model = Map.of("user", Map.of("account", "gugu1"), "user2",
                Map.of("account", "gugu2"));

        jsonView.render(model, request, response);

        assertThat(stringWriter.toString()).contains(
                "\"user\":{\"account\":\"gugu1\"}",
                "\"user2\":{\"account\":\"gugu2\"}");
    }

    @DisplayName("Content-Type을 application/json;charset=UTF-8로 설정한다.")
    @Test
    void setContentType() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));

        final Map<String, Object> model = Map.of("user", Map.of("account", "gugu"));

        jsonView.render(model, request, response);

        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }
}
