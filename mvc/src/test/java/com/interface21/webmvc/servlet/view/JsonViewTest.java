package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JsonViewTest {

    private final JsonView view = new JsonView();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter output;

    @BeforeEach
    void setUp() throws Exception {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        output = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(output));
    }

    @Test
    @DisplayName("모델이 하나면 키를 제외하고 값만 JSON으로 응답한다")
    void rendersSingleValue() throws Exception {
        view.render(Map.of("user", Map.of("account", "gugu")), request, response);

        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        verify(response).setCharacterEncoding("UTF-8");
        assertThat(objectMapper.readTree(output.toString()))
                .isEqualTo(objectMapper.readTree("{\"account\":\"gugu\"}"));
    }

    @Test
    @DisplayName("모델이 여러 개면 Map 전체를 JSON으로 응답한다")
    void rendersMultipleValues() throws Exception {
        final var model = Map.of("user", Map.of("account", "gugu"), "count", 1);

        view.render(model, request, response);

        assertThat(objectMapper.readTree(output.toString()))
                .isEqualTo(objectMapper.readTree("{\"user\":{\"account\":\"gugu\"},\"count\":1}"));
    }

    @Test
    @DisplayName("한글과 따옴표를 JSON으로 응답한다")
    void rendersUnicodeAndEscapesQuotes() throws Exception {
        view.render(Map.of("message", "안녕 \"MVC\""), request, response);

        assertThat(objectMapper.readTree(output.toString()).textValue())
                .isEqualTo("안녕 \"MVC\"");
    }
}
