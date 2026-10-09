package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JsonViewTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("모델 값이 하나면 값 자체를 JSON으로 응답한다")
    void givenSingleModelValue_whenRenders_thenWritesValueAsJson() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var stringWriter = new StringWriter();
        final var printWriter = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(printWriter);

        final var jsonView = new JsonView();

        jsonView.render(
                Map.of("name", "gugu"),
                request,
                response
        );

        final var json = objectMapper.readTree(stringWriter.toString());

        assertThat(json.asText()).isEqualTo("gugu");
    }

    @Test
    @DisplayName("모델 값이 두 개 이상이면 모델을 JSON 객체로 응답한다")
    void givenMultipleModelValues_whenRenders_thenWritesModelAsJson() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var stringWriter = new StringWriter();
        final var printWriter = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(printWriter);

        final var jsonView = new JsonView();

        jsonView.render(
                Map.of(
                        "name", "gugu",
                        "age", 20
                ),
                request,
                response
        );

        final var json = objectMapper.readTree(stringWriter.toString());

        assertThat(json.get("name").asText()).isEqualTo("gugu");
        assertThat(json.get("age").asInt()).isEqualTo(20);
    }

    @Test
    @DisplayName("JSON 응답의 Content-Type은 UTF-8을 명시한다")
    void givenJsonView_whenRenders_thenSetsJsonContentType() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var printWriter = mock(PrintWriter.class);

        when(response.getWriter()).thenReturn(printWriter);

        final var jsonView = new JsonView();

        jsonView.render(
                Map.of("name", "gugu"),
                request,
                response
        );

        verify(response).setContentType("application/json;charset=UTF-8");
    }
}
