package com.interface21.webmvc.servlet.view;

import com.interface21.web.http.MediaType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

    @Test
    void JSON_Content_Type으로_응답한다() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));
        final var jsonView = new JsonView();

        jsonView.render(Map.of("account", "gugu"), request, response);

        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    @Test
    void 모델_값이_하나면_값만_JSON으로_응답한다() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var body = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(body));
        final var jsonView = new JsonView();

        jsonView.render(Map.of("user", Map.of("account", "gugu")), request, response);

        assertThat(body).hasToString("{\"account\":\"gugu\"}");
    }

    @Test
    void 모델_값이_둘_이상이면_모델을_그대로_JSON으로_응답한다() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var body = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(body));
        final var jsonView = new JsonView();
        final var model = new LinkedHashMap<String, Object>();
        model.put("account", "gugu");
        model.put("email", "gugu@woowahan.com");

        jsonView.render(model, request, response);

        assertThat(body).hasToString("{\"account\":\"gugu\",\"email\":\"gugu@woowahan.com\"}");
    }

    @Test
    void 모델이_비어_있으면_빈_JSON_객체로_응답한다() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var body = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(body));
        final var jsonView = new JsonView();

        jsonView.render(Map.of(), request, response);

        assertThat(body).hasToString("{}");
    }
}
