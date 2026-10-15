package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import org.junit.jupiter.api.Test;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JsonViewTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpServletResponse response = mock(HttpServletResponse.class);

    @Test
    void rendersSingleValueWithoutModelKey() throws Exception {
        final var body = render(Map.of("user", Map.of("account", "티모")));

        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(objectMapper.readTree(body))
                .isEqualTo(objectMapper.readTree("{\"account\":\"티모\"}"));
    }

    @Test
    void rendersMultipleValuesAsMap() throws Exception {
        final var body = render(Map.of("account", "timo", "id", 1));

        assertThat(objectMapper.readTree(body))
                .isEqualTo(objectMapper.readTree("{\"account\":\"timo\",\"id\":1}"));
    }

    @Test
    void rendersEmptyModelAsEmptyObject() throws Exception {
        assertThat(render(Map.of())).isEqualTo("{}");
    }

    private String render(final Map<String, ?> model) throws Exception {
        final var body = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(body));
        new JsonView().render(model, mock(HttpServletRequest.class), response);
        return body.toString();
    }
}
