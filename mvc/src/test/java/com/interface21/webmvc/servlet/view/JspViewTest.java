package com.interface21.webmvc.servlet.view;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JspViewTest {

    @DisplayName("viewName에 해당하는 JSP로 forward한다.")
    @Test
    void forward() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var requestDispatcher = mock(RequestDispatcher.class);

        when(request.getRequestDispatcher("/register.jsp")).thenReturn(requestDispatcher);

        new JspView("/register.jsp").render(Map.of(), request, response);

        verify(requestDispatcher).forward(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @DisplayName("model에 담긴 값을 request 속성으로 옮긴다.")
    @Test
    void renderModel() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var requestDispatcher = mock(RequestDispatcher.class);

        when(request.getRequestDispatcher("/register.jsp")).thenReturn(requestDispatcher);

        new JspView("/register.jsp").render(Map.of("id", "gugu"), request, response);

        verify(request).setAttribute("id", "gugu");
    }

    @DisplayName("viewName이 redirect: 로 시작하면 접두사를 떼고 리다이렉트한다.")
    @Test
    void redirect() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        new JspView("redirect:/index.jsp").render(Map.of(), request, response);

        verify(response).sendRedirect("/index.jsp");
        verify(request, never()).getRequestDispatcher(anyString());
    }

    @DisplayName("리다이렉트할 때는 model을 request에 담지 않는다.")
    @Test
    void redirectDoesNotRenderModel() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        new JspView("redirect:/index.jsp").render(Map.of("id", "gugu"), request, response);

        verify(request, never()).setAttribute(anyString(), any());
    }
}
