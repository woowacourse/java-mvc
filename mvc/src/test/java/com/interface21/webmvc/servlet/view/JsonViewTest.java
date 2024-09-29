package com.interface21.webmvc.servlet.view;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JsonViewTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private PrintWriter writer;

    @BeforeEach
    void setUp() throws IOException {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);
    }

    @DisplayName("JSON 응답 시 ContentType은 application/json;charset=UTF-8이다.")
    @Test
    void contentTypeTest() throws Exception {
        // given
        JsonView jsonView = new JsonView();

        // when
        jsonView.render(new HashMap<>(), request, response);

        // then
        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    @Test
    @DisplayName("Map 형태의 응답을 JSON 형태로 변환하여 응답한다..")
    void renderJson() throws Exception {
        // given
        JsonView jsonView = new JsonView();
        Map<String, Object> model = new HashMap<>();
        model.put("name", "John");
        model.put("age", 30);

        // when
        jsonView.render(model, request, response);

        // then
        ObjectMapper objectMapper = new ObjectMapper();
        String expectedJson = objectMapper.writeValueAsString(model);

        verify(writer).write(expectedJson);
    }
}
