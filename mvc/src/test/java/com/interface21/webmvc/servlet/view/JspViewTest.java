package com.interface21.webmvc.servlet.view;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.junit.jupiter.api.Test;

class JspViewTest {

    @Test
    void forwardWithModel() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var requestDispatcher = mock(RequestDispatcher.class);
        final var view = new JspView("/test.jsp");

        when(request.getRequestDispatcher("/test.jsp")).thenReturn(requestDispatcher);

        view.render(Map.of("id", "gugu"), request, response);

        final var renderOrder = inOrder(request, requestDispatcher);
        renderOrder.verify(request).setAttribute("id", "gugu");
        renderOrder.verify(requestDispatcher).forward(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    void redirect() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var view = new JspView("redirect:/index.jsp");

        view.render(Map.of(), request, response);

        verify(response).sendRedirect("/index.jsp");
        verify(request, never()).getRequestDispatcher(anyString());
    }
}
