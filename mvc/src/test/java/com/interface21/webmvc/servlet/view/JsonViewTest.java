package com.interface21.webmvc.servlet.view;

import com.interface21.web.http.MediaType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JsonViewTest {

    @DisplayName("model에 값이 하나면 그 값 자체를 JSON으로 응답한다.")
    @Test
    void renderSingleValue() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        new JsonView().render(Map.of("user", Map.of("account", "gugu")), request, response);

        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(writer.toString()).isEqualTo("{\"account\":\"gugu\"}");
    }

    @DisplayName("model에 값이 두 개 이상이면 Map 그대로 JSON으로 응답한다.")
    @Test
    void renderMultipleValues() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        final var model = new LinkedHashMap<String, Object>();
        model.put("account", "gugu");
        model.put("email", "gugu@email.com");

        new JsonView().render(model, request, response);

        assertThat(writer.toString()).isEqualTo("{\"account\":\"gugu\",\"email\":\"gugu@email.com\"}");
    }
}
