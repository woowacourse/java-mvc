package com.interface21.webmvc.servlet.view;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
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

    @Test
    void testRenderSingleModelObject() throws Exception {
        Map<String, Object> model = new HashMap<>();
        model.put("name", "John");

        jsonView.render(model, request, response);

        assertEquals("{\"name\":\"John\"}", responseWriter.toString().trim());
        verify(response).setContentType("application/json;charset=UTF-8");
    }

    @Test
    void testRenderMultipleModelObjects() throws Exception {
        Map<String, Object> model = new HashMap<>();
        model.put("name", "John");
        model.put("age", 30);

        jsonView.render(model, request, response);

        assertEquals("{\"name\":\"John\",\"age\":30}", responseWriter.toString().trim());
        verify(response).setContentType("application/json;charset=UTF-8");
    }

    @Test
    void testRenderEmptyModel() throws Exception {
        Map<String, Object> model = new HashMap<>();

        jsonView.render(model, request, response);

        assertEquals("{}", responseWriter.toString().trim());
        verify(response).setContentType("application/json;charset=UTF-8");
    }
}
