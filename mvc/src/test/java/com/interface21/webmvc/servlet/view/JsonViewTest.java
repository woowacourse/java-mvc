package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.interface21.web.http.MediaType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import org.junit.jupiter.api.Test;

class JsonViewTest {

    @Test
    void responseOneModel() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var body = new StringWriter();
        final Map<String, ?> model = Map.of("name", "수민");

        when(response.getWriter()).thenReturn(new PrintWriter(body));
        JsonView jsonView = new JsonView();
        jsonView.render(model, request, response);

        assertThat(body.toString()).isEqualTo("\"수민\"");
        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

}
