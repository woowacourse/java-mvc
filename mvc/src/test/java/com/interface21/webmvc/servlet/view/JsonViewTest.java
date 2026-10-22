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

    private final JsonView jsonView = new JsonView();
    private final HttpServletRequest request = mock(HttpServletRequest.class);

    @Test
    void rendersSingleModelValueAsJson() throws Exception {
        final var response = mock(HttpServletResponse.class);
        final var responseBody = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseBody));

        jsonView.render(Map.of("message", "hello"), request, response);

        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(responseBody.toString()).isEqualTo("\"hello\"");
    }

    @Test
    void rendersMultipleModelValuesAsJsonObject() throws Exception {
        final var response = mock(HttpServletResponse.class);
        final var responseBody = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseBody));

        jsonView.render(Map.of("name", "gugu", "age", 20), request, response);

        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(responseBody.toString()).isIn(
                "{\"name\":\"gugu\",\"age\":20}",
                "{\"age\":20,\"name\":\"gugu\"}"
        );
    }
}
