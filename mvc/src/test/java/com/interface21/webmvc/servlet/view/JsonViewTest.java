package com.interface21.webmvc.servlet.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JsonViewTest {

    @Test
    void model이_하나면_값만_JSON으로_응답한다() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        final var writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        new JsonView().render(Map.of("name", "moka"), request, response);

        assertThat(writer.toString()).isEqualTo("\"moka\"");
    }

    @Test
    void model이_여러개면_Map을_JSON으로_응답한다() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        final var writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        new JsonView().render(Map.of("name", "moka", "age", 27), request, response);

        assertThat(writer.toString())
                .contains("\"name\":\"moka\"")
                .contains("\"age\":27");
    }
}
