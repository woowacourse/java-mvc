package com.interface21.webmvc.servlet.view;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

class JspViewTest {

    @Test
    @DisplayName("JSP 경로로 요청과 응답을 전달한다")
    void forward_to_jsp() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(dispatcher);

        new JspView("/index.jsp").render(Map.of(), request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("모델을 request 속성에 등록한다")
    void expose_model_as_request_attributes() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(dispatcher);

        new JspView("/index.jsp").render(Map.of("name", "gugu", "age", 20), request, response);

        verify(request).setAttribute("name", "gugu");
        verify(request).setAttribute("age", 20);
    }

    @Test
    @DisplayName("redirect 접두사를 제거한 경로로 리다이렉트하고 forward하지 않는다")
    void redirect() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        new JspView("redirect:/index.jsp").render(Map.of(), request, response);

        verify(response).sendRedirect("/index.jsp");
        verifyNoInteractions(request);
    }
}
