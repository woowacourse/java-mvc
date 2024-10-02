package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JsonViewTest {

    private final View jsonView = new JsonView();

    @DisplayName("model 내용을 json 형태로 응답한다.")
    @Test
    void render1() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        Map<String, String> model = new LinkedHashMap<>();
        model.put("name", "wiib");
        model.put("category", "backend");

        jsonView.render(model, request, response);

        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(stringWriter.toString()).isEqualTo("{\"name\":\"wiib\",\"category\":\"backend\"}");
    }

    @DisplayName("데이터가 1개면 값을 그대로 반환한다.")
    @Test
    void render2() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        jsonView.render(Map.of("name", "wiib"), request, response);

        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(stringWriter.toString()).isEqualTo("\"wiib\"");
    }

}
