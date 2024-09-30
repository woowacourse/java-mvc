package com.interface21.webmvc.servlet.view;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.util.Map;
import java.util.stream.Stream;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.interface21.web.http.MediaType;

class JsonViewTest {

    @ParameterizedTest
    @MethodSource
    @DisplayName("Render json data.")
    void render(Map<String, Object> model, String expected) throws Exception {
        // given
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);
        var printWriter = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(printWriter);

        var sut = new JsonView();

        // when
        sut.render(model, request, response);

        // then
        verify(printWriter).write(expected);
        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    static Stream<Arguments> render() {
        var value1 = "Arachneee";
        var value2 = "{\"account\":\"Arachneee\", \"company\":\"woowacourse\"";
        return Stream.of(
                Arguments.of(
                        Map.of("account", "Arachneee"),
                        value1),
                Arguments.of(
                        Map.of("account", "Arachneee", "company", "woowacourse"),
                        value2));
    }
}
