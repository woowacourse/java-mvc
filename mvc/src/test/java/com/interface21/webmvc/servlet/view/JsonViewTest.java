package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JsonViewTest {

    private final JsonView jsonView = new JsonView();
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final StringWriter body = new StringWriter();

    @Test
    void rendersSingleModelValueDirectly() throws Exception {
        when(response.getWriter()).thenReturn(new PrintWriter(body));

        jsonView.render(Map.of("message", "hello"), request, response);

        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(new ObjectMapper().readTree(body.toString()).asText()).isEqualTo("hello");
    }

    @Test
    void rendersMultipleModelValuesAsAnObject() throws Exception {
        when(response.getWriter()).thenReturn(new PrintWriter(body));

        jsonView.render(Map.of("name", "샤를", "id", 1), request, response);

        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(new ObjectMapper().readTree(body.toString()))
                .isEqualTo(new ObjectMapper().readTree("{\"name\":\"샤를\",\"id\":1}"));
    }
}
