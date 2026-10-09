package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.TreeMap;
import org.junit.jupiter.api.Test;

class JsonViewTest {

    @Test
    void renderSingleValue() throws Exception {
        //given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var body = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(body, true));

        //when
        new JsonView().render(Map.of("user", Map.of("account", "gugu")), request, response);

        //then
        assertThat(body.toString()).isEqualTo("{\"account\":\"gugu\"}");
    }

    @Test
    void renderMultipleValues() throws Exception {
        //given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var body = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(body, true));

        //when
        new JsonView().render(new TreeMap<>(Map.of("account", "gugu", "count", 1)), request, response);

        //then
        assertThat(body.toString()).isEqualTo("{\"account\":\"gugu\",\"count\":1}");
    }
}
