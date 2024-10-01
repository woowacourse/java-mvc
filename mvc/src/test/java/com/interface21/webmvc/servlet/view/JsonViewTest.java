package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class JsonViewTest {

    private static final String APPLICATION_JSON_CHARSET_UTF_8 = "application/json;charset=UTF-8";

    private JsonView jsonView;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter responseWriter;
    private PrintWriter printWriter;

    @BeforeEach
    void setUp() throws Exception {
        jsonView = new JsonView();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        responseWriter = new StringWriter();
        printWriter = new PrintWriter(responseWriter);

        when(response.getWriter()).thenReturn(printWriter);
    }

    @Test
    @DisplayName("단일 모델 값이 JSON으로 반환되는지 확인한다.")
    void render_singleValue() throws Exception {
        // given
        Map<String, Object> model = new HashMap<>();
        model.put("name", "Ash");

        // when
        jsonView.render(model, request, response);
        printWriter.flush();

        // then
        assertThat(responseWriter.toString()).hasToString("\"Ash\"");
    }

    @Test
    @DisplayName("다중 모델 값이 JSON으로 반환되는지 확인한다.")
    void render_multipleValues() throws Exception {
        // given
        Map<String, Object> model = new HashMap<>();
        model.put("name", "Ash");
        model.put("age", 25);

        // when
        jsonView.render(model, request, response);
        printWriter.flush();

        ObjectMapper objectMapper = new ObjectMapper();
        String expectedJson = objectMapper.writeValueAsString(model);

        // then
        assertThat(responseWriter.toString()).hasToString(expectedJson);
    }

    @Test
    @DisplayName("응답의 ContentType이 APPLICATION_JSON_CHARSET_UTF_8로 설정되는지 확인한다.")
    void render_ContentType() throws Exception {
        // given
        Map<String, Object> model = new HashMap<>();

        // when
        jsonView.render(model, request, response);
        printWriter.flush();

        // then
        verify(response).setContentType(APPLICATION_JSON_CHARSET_UTF_8);
    }
}
