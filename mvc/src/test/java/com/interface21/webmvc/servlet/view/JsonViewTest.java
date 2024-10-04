package com.interface21.webmvc.servlet.view;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JsonViewTest {

    @Test
    @DisplayName("model 개수가 1개이면 문자열을 반환한다.")
    void return_string_value() throws Exception {
        // given
        final JsonView jsonView = new JsonView();
        final Map<String, ?> model = Map.of("name", "lemon");
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        // when
        jsonView.render(model, request, response);

        // then
        verify(writer).write("lemon");
    }

    @Test
    @DisplayName("model이 비어있으면 빈 값을 반환한다.")
    void return_null() throws Exception {
        // given
        final JsonView jsonView = new JsonView();
        final Map<String, ?> model = Map.of();
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        // when
        jsonView.render(model, request, response);

        // then
        verify(writer).write("");
    }

    @Test
    @DisplayName("model 개수가 2개 이상이면 Json을 반환한다.")
    void return_json() throws Exception {
        // given
        final JsonView jsonView = new JsonView();
        final Map<String, String> model = new HashMap<>();
        model.put("name", "lemon");
        model.put("age", "25");
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        // when
        jsonView.render(model, request, response);

        // then
        verify(writer).write("{\"name\":\"lemon\",\"age\":\"25\"}");
    }
}
