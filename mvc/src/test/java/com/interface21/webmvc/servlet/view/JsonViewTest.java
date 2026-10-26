package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JsonViewTest {

    private final JsonView jsonView = new JsonView();
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);

    @Test
    void 모델_값이_하나면_이름을_제외하고_JSON으로_응답한다() throws Exception {
        // given
        StringWriter responseBody = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseBody));

        // when
        jsonView.render(Map.of("name", "junie"), request, response);

        // then
        assertThat(responseBody.toString()).isEqualTo("\"junie\"");
        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    @Test
    void 모델_값이_여러개면_이름을_포함한_JSON으로_응답한다() throws Exception {
        // given
        StringWriter responseBody = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseBody));

        // when
        jsonView.render(Map.of("name", "junie", "age", 25), request, response);

        // then
        JsonNode json = new ObjectMapper().readTree(responseBody.toString());
        assertThat(json.get("name").asText()).isEqualTo("junie");
        assertThat(json.get("age").asInt()).isEqualTo(25);
        assertThat(json.size()).isEqualTo(2);
    }

    @Test
    void 빈_모델은_빈_JSON_객체로_응답한다() throws Exception {
        // given
        StringWriter responseBody = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseBody));

        // when
        jsonView.render(Map.of(), request, response);

        // then
        assertThat(responseBody.toString()).isEqualTo("{}");
    }

    @Test
    void 모델_값이_null이어도_JSON_null로_응답한다() throws Exception {
        // given
        StringWriter responseBody = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseBody));

        // when
        jsonView.render(Collections.singletonMap("value", null), request, response);

        // then
        assertThat(responseBody.toString()).isEqualTo("null");
    }

    @Test
    void 문자열은_JSON_규칙에_맞게_이스케이프한다() throws Exception {
        // given
        StringWriter responseBody = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseBody));

        // when
        jsonView.render(Map.of("message", "line 1\n\"line 2\""), request, response);

        // then
        assertThat(responseBody.toString()).isEqualTo("\"line 1\\n\\\"line 2\\\"\"");
    }
}
