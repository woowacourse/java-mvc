package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.interface21.web.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import org.junit.jupiter.api.Test;

class JsonViewTest {

    @Test
    void 모델이_하나면_값만_JSON으로_응답한다() throws Exception {
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);

        var body = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(body));

        var model = Map.of("user", Map.of("account", "pino"));

        new JsonView().render(model, request, response);

        assertThat(body.toString()).isEqualTo("{\"account\":\"pino\"}");
        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }
    @Test
    void 모델이_여러개면_Map_전체를_JSON으로_응답한다() throws Exception {
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);
        var body = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(body));
        var model = Map.of("account", "피노", "count", 2);

        new JsonView().render(model, request, response);

        var mapper = new ObjectMapper();
        assertThat(mapper.readTree(body.toString())).isEqualTo(mapper.valueToTree(model));
        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    @Test
    void 빈_모델은_빈_JSON_객체로_응답한다() throws Exception {
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);
        var body = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(body));

        new JsonView().render(Map.of(), request, response);

        assertThat(body.toString()).isEqualTo("{}");
    }
}
