package com.interface21.webmvc.servlet.view.impl;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JsonViewTest {

    @Test
    @DisplayName("Model에 데이터가 하나인 경우")
    void renderWithOneData() throws IOException {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        PrintWriter writer = mock(PrintWriter.class);
        given(response.getWriter()).willReturn(writer);

        JsonView jsonView = new JsonView();
        Map<String, String> model = Map.of("name", "sancho");

        // when
        jsonView.render(model, request, response);

        // then
        verify(response).setContentType("application/json;charset=UTF-8");
        verify(writer).write("\"sancho\"");
    }

    @Test
    @DisplayName("Model에 데이터가 여러 개인 경우")
    void renderWithMultipleData() throws IOException {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        PrintWriter writer = mock(PrintWriter.class);
        given(response.getWriter()).willReturn(writer);

        JsonView jsonView = new JsonView();
        Map<String, String> model = Map.of("name", "sancho", "position", "backend");

        // when
        jsonView.render(model, request, response);

        // then
        verify(response).setContentType("application/json;charset=UTF-8");
        verify(writer).write(new ObjectMapper().writeValueAsString(model));
    }
}
