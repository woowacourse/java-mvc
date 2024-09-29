package com.interface21.webmvc.servlet.view;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JsonViewTest {

    @Test
    @DisplayName("JsonView가 모델을 JSON으로 직렬화하고 올바른 ContentType으로 응답한다.")
    void renderJson() throws Exception {
        // given
        JsonView jsonView = new JsonView();
        Map<String, Object> model = new HashMap<>();
        model.put("name", "John");
        model.put("age", 30);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        PrintWriter writer = mock(PrintWriter.class);

        when(response.getWriter()).thenReturn(writer);

        // when
        jsonView.render(model, request, response);

        // then
        ObjectMapper objectMapper = new ObjectMapper();
        String expectedJson = objectMapper.writeValueAsString(model);

        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        verify(writer).write(expectedJson);
    }
}
