package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.io.CharArrayWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import com.interface21.web.http.MediaType;

class JsonViewTest {

    @Test
    @DisplayName("Model의 값이 1 개라면, Model의 값이 1개라면 평문을 반환한다.")
    void return_plaintext_when_model_size_is_one() throws Exception {
        // given
        final JsonView view = new JsonView();
        final HttpServletRequest request = new MockHttpServletRequest();
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final CharArrayWriter charArrayWriter = new CharArrayWriter();
        final PrintWriter printWriter = new PrintWriter(charArrayWriter);
        given(response.getContentType()).willReturn(MediaType.APPLICATION_JSON_UTF8_VALUE);
        given(response.getWriter()).willReturn(printWriter);

        // when
        view.render(new HashMap<>(Map.of("fram", "handsome")), request, response);

        // then
        assertThat(charArrayWriter.toString()).hasToString("handsome");
    }

    @Test
    @DisplayName("Model의 값이 2개 이상이라면, Json바디를 반환한다.")
    void return_json_body() throws Exception {
        // given
        final JsonView view = new JsonView();
        final HttpServletRequest request = new MockHttpServletRequest();
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final CharArrayWriter charArrayWriter = new CharArrayWriter();
        final PrintWriter printWriter = new PrintWriter(charArrayWriter);
        given(response.getContentType()).willReturn(MediaType.APPLICATION_JSON_UTF8_VALUE);
        given(response.getWriter()).willReturn(printWriter);

        // when
        view.render(new HashMap<>(Map.of("fram", "handsome", "hello", "world")), request, response);

        // then
        assertThat(charArrayWriter.toString()).hasToString("{\"fram\":\"handsome\",\"hello\":\"world\"}");
    }
}
