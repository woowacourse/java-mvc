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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;

class JsonViewTest {

    @Test
    @DisplayName("Render one json data.")
    void render() throws Exception {
        // given
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);
        var printWriter = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(printWriter);
        var model = Map.of("account", "Arachneee");

        var sut = new JsonView();

        // when
        sut.render(model, request, response);

        // then
        var objectMapper = new ObjectMapper();
        verify(printWriter).write(objectMapper.writeValueAsString("Arachneee"));
        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }


    @Test
    @DisplayName("Render mulitple json data.")
    void render_multiple() throws Exception {
        // given
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);
        var printWriter = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(printWriter);
        var model = Map.of("account", "Arachneee", "company", "woowacourse");
        var sut = new JsonView();

        //when
        sut.render(model, request, response);

        // then
        var objectMapper = new ObjectMapper();
        verify(printWriter).write(objectMapper.writeValueAsString(model));
        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }
}
