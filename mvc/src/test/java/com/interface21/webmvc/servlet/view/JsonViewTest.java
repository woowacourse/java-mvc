package com.interface21.webmvc.servlet.view;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

class JsonViewTest {

    private JsonView jsonView;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter responseWriter;

    @BeforeEach
    void setUp() throws Exception {
        jsonView = new JsonView();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        responseWriter = new StringWriter();

        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));
    }

    @DisplayName("개수가 1개일 때는 값을 그대로 반환한다.")
    @Test
    void renderSingleModelObject() throws Exception {
        Map<String, Object> model = new HashMap<>();
        model.put("name", "John");

        jsonView.render(model, request, response);

        assertEquals("\"John\"", responseWriter.toString().trim());
        verify(response).setContentType("application/json;charset=UTF-8");
    }

    @DisplayName("개수가 2개 이상일 때는 map 형태로 반환한다.")
    @Test
    void testRenderMultipleModelObjects() throws Exception {
        Map<String, Object> model = new HashMap<>();
        model.put("name", "John");
        model.put("age", 30);

        jsonView.render(model, request, response);

        assertEquals("{\"name\":\"John\",\"age\":30}", responseWriter.toString().trim());
        verify(response).setContentType("application/json;charset=UTF-8");
    }

    @DisplayName("빈 응답을 반환한다.")
    @Test
    void renderEmptyModel() throws Exception {
        Map<String, Object> model = new HashMap<>();

        jsonView.render(model, request, response);

        assertEquals("{}", responseWriter.toString().trim());
        verify(response).setContentType("application/json;charset=UTF-8");
    }
}
