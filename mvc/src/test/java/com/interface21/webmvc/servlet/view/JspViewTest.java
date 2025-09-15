package com.interface21.webmvc.servlet.view;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.mockito.Mockito.*;

class JspViewTest {

    @Test
    void renderWithRedirect() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var jspView = new JspView("redirect:/index.jsp");

        jspView.render(Map.of(), request, response);

        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    void renderWithForward() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var requestDispatcher = mock(RequestDispatcher.class);
        final var jspView = new JspView("/index.jsp");

        when(request.getRequestDispatcher("/index.jsp")).thenReturn(requestDispatcher);

        jspView.render(Map.of("id", "gugu"), request, response);

        verify(request).setAttribute("id", "gugu");
        verify(requestDispatcher).forward(request, response);
    }
}
