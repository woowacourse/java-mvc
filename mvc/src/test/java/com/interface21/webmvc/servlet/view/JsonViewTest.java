package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JsonViewTest {

    private JsonView jsonView;

    @BeforeEach
    void setUp() {
        this.jsonView = new JsonView();
    }

    @Test
    @DisplayName("데이터가 1개인 JSON 응답을 생성한다.")
    void renderOneData() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        // when
        Map<String, ?> model = Map.of("test", "value");
        jsonView.render(model, request, response);

        // then
        assertThat(stringWriter).hasToString("\"value\"");
    }

    @Test
    @DisplayName("데이터가 2개 이상인 JSON 응답을 생성한다.")
    void renderOverTwoData() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        // when
        Map<String, ?> model = Map.of("test", "value", "hello", "world");
        jsonView.render(model, request, response);

        // then
        assertThat(stringWriter).hasToString("{\"test\":\"value\",\"hello\":\"world\"}");
    }

    @Test
    @DisplayName("데이터가 없다면 빈 JSON 응답을 생성한다.")
    void renderNoData() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        // when
        Map<String, ?> model = Map.of();
        jsonView.render(model, request, response);

        // then
        assertThat(stringWriter).hasToString("{}");
    }
}
