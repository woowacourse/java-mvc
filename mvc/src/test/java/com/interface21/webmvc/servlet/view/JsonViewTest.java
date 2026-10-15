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

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void rendersTheOnlyModelValueAsTheJsonResponseBody() throws Exception {
        final var responseBody = new StringWriter();
        final var response = mock(HttpServletResponse.class);
        when(response.getWriter()).thenReturn(new PrintWriter(responseBody));

        new JsonView().render(Map.of("account", "gugu"), mock(HttpServletRequest.class), response);

        assertThat(objectMapper.readTree(responseBody.toString()).asText()).isEqualTo("gugu");
        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        verify(response).setCharacterEncoding("UTF-8");
    }

    @Test
    void rendersMultipleModelValuesAsAJsonObject() throws Exception {
        final var responseBody = new StringWriter();
        final var response = mock(HttpServletResponse.class);
        when(response.getWriter()).thenReturn(new PrintWriter(responseBody));

        new JsonView().render(Map.of("account", "gugu", "role", "user"), mock(HttpServletRequest.class), response);

        final var json = objectMapper.readTree(responseBody.toString());
        assertThat(json.get("account").asText()).isEqualTo("gugu");
        assertThat(json.get("role").asText()).isEqualTo("user");
    }
}
