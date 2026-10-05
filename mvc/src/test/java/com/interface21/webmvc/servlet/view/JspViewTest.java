package com.interface21.webmvc.servlet.view;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class JspViewTest {

    @Test
    void forwardWithModelAttributes() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/users.jsp")).thenReturn(requestDispatcher);
        final var view = new JspView("/users.jsp");

        view.render(Map.of("id", "gugu"), request, response);

        final var order = inOrder(request, requestDispatcher);
        order.verify(request).setAttribute("id", "gugu");
        order.verify(requestDispatcher).forward(request, response);
        verifyNoInteractions(response);
    }

    @Test
    void redirectToUrlWithoutPrefix() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var view = new JspView("redirect:/users?id=gugu");

        view.render(Map.of(), request, response);

        verify(response).sendRedirect("/users?id=gugu");
        verifyNoInteractions(request);
    }
}
