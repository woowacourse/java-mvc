package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonViewTest {

    private JsonView jsonView;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter stringWriter;

    @BeforeEach
    void setup() throws IOException {
        jsonView = new JsonView();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
    }

    @DisplayName("Json으로 응답할 때 ContentType은 MediaType.APPLICATION_JSON_UTF8_VALUE으로 반환해야 한다.")
    @Test
    void test_Render_ContentType_ApplicationJson() throws Exception {
        // given
        final Map<String, ?> model = Map.of("key", "value");

        // when
        jsonView.render(model, request, response);

        // then
        verify(response).setContentType("application/json;charset=UTF-8");
        verify(response).setCharacterEncoding("UTF-8");
    }

    @DisplayName("")
    @Test
    void test_Render_When_ModelSize_One() throws Exception {
        // given
        final Map<String, ?> model = Map.of("key", "value");

        // when
        jsonView.render(model, request, response);
        final String actual = stringWriter.toString();

        // then
        final String expected = "\"value\"";

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("")
    @Test
    void test_Render_When_ModelSize_Over_Two() throws Exception {
        // given
        final Map<String, ?> model = Map.of("key1", "value1", "key2", "value2");

        // when
        jsonView.render(model, request, response);
        final String actual = stringWriter.toString();

        // then
        final String expected = new ObjectMapper().writeValueAsString(model);

        assertThat(actual).isEqualTo(expected);
    }
}
