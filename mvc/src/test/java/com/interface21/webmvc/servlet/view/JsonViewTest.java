package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

class JsonViewTest {

    private JsonView jsonView;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter stringWriter;

    @BeforeEach
    void setUp() throws Exception {
        jsonView = new JsonView();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
    }

    @DisplayName("model에 데이터가 1개면 값을 그대로 반환한다.")
    @Test
    void render_singleValue() throws Exception {
        Map<String, String> model = Map.of("key", "value");

        jsonView.render(model, request, response);

        String expected = "\"value\"";
        assertThat(stringWriter.toString()).isEqualTo(expected);
        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    @DisplayName("model에 데이터가 2개 이상이면 Map 형태 그대로 json으로 변환해서 반환한다.")
    @Test
    void render_multipleValue() throws Exception {
        Map<String, String> model = Map.of(
                "key1", "value1",
                "key2", "value2"
        );

        jsonView.render(model, request, response);

        String expected = new ObjectMapper().writeValueAsString(model);
        assertThat(stringWriter.toString()).isEqualTo(expected);
        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    @DisplayName("model에 데이터가 없으면 빈 json object를 반환한다.")
    @Test
    void render_emptyValue() throws Exception {
        Map<String, Object> model = Map.of();

        jsonView.render(model, request, response);

        String expected = "{}";
        assertThat(stringWriter.toString()).isEqualTo(expected);
        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }
}
