package com.interface21.webmvc.servlet.mvc.view;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.view.JsonView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("JsonView 테스트")
class JsonViewTest {

    @DisplayName("단일 데이터 처리 테스트")
    @Test
    void renderSingleDataTest() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        JsonView jsonView = new JsonView();
        Map<String, Object> model = new HashMap<>();
        model.put("key1", "value1");

        // when
        jsonView.render(model, request, response);

        // then
        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        verify(writer).write(new ObjectMapper().writeValueAsString("value1"));
    }

    @DisplayName("복수 데이터 처리 테스트")
    @Test
    void renderMultipleDataTest() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        JsonView jsonView = new JsonView();
        Map<String, Object> model = new HashMap<>();
        model.put("key1", "value1");
        model.put("key2", "value2");

        // when
        jsonView.render(model, request, response);

        // then
        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        verify(writer).write(new ObjectMapper().writeValueAsString(model));
    }
}
