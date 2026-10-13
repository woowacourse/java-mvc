package com.interface21.webmvc.servlet.view;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JspViewTest {

    @Test
    void forward() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(requestDispatcher);

        final var jspView = new JspView("/index.jsp");
        jspView.render(Map.of("name", "gugu"), request, response);

        verify(request).setAttribute("name", "gugu");
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void redirect() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        final var jspView = new JspView("redirect:/index.jsp");
        jspView.render(Map.of(), request, response);

        verify(response).sendRedirect("/index.jsp");
    }
}
