package com.interface21.webmvc.servlet.view;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JspViewTest {

    @Test
    void redirect_접두사가_있으면_접두사를_뗀_경로로_리다이렉트한다() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var jspView = new JspView("redirect:/index.jsp");

        jspView.render(Map.of(), request, response);

        verify(response).sendRedirect("/index.jsp");
        verify(request, never()).getRequestDispatcher(anyString());
    }

    @Test
    void redirect_접두사가_없으면_JSP로_포워드한다() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(requestDispatcher);
        final var jspView = new JspView("/index.jsp");

        jspView.render(Map.of(), request, response);

        verify(requestDispatcher).forward(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    void 포워드할_때_모델을_요청_속성에_담는다() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(mock(RequestDispatcher.class));
        final var jspView = new JspView("/index.jsp");

        jspView.render(Map.of("id", "gugu"), request, response);

        verify(request).setAttribute("id", "gugu");
    }
}
