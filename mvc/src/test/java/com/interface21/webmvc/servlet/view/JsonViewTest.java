package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JsonViewTest {

    @Test
    @DisplayName("모델이 하나면 해당 값을 JSON으로 렌더링한다")
    void renderSingleValue() throws Exception {
        final var response = mock(HttpServletResponse.class);
        final var body = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(body));

        new JsonView().render(Map.of("user", Map.of("account", "gugu")),
                mock(HttpServletRequest.class), response);

        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        verify(response).setCharacterEncoding(StandardCharsets.UTF_8);
        final var json = new ObjectMapper().readTree(body.toString());
        assertThat(json).isEqualTo(new ObjectMapper().readTree("{\"account\":\"gugu\"}"));
    }

    @Test
    @DisplayName("모델 항목이 2개 이상이면 Map 형태 그대로 JSON으로 변환해서 반환한다")
    void renderMultipleValues() throws Exception {
        final var response = mock(HttpServletResponse.class);
        final var body = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(body));

        new JsonView().render(Map.of("account", "gugu", "message", "안녕하세요"),
                mock(HttpServletRequest.class), response);

        final var json = new ObjectMapper().readTree(body.toString());
        assertThat(json).isEqualTo(new ObjectMapper().readTree(
                "{\"account\":\"gugu\",\"message\":\"안녕하세요\"}"));
    }
}
