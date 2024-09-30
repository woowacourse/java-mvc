package com.interface21.webmvc.servlet.view;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JsonViewTest {

    @Test
    @DisplayName("model 개수가 1개이면 문자열을 반환한다.")
    void return_string_value() throws Exception {
        // given
        final JsonView jsonView = new JsonView();
        final Map<String, ?> model = Map.of("name", "lemon");
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        when(response.getWriter()).thenReturn(new PrintWriter(System.out));

        // when & then
        assertDoesNotThrow(() -> jsonView.render(model, request, response));
    }
}
