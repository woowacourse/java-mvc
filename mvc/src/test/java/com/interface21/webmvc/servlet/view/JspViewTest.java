package com.interface21.webmvc.servlet.view;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.mockito.Mockito.*;

class JspViewTest {

    @Test
    void forwardsToJspWithModelAttributes() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/users.jsp")).thenReturn(dispatcher);

        new JspView("/users.jsp").render(Map.of("id", "gugu", "count", 2), request, response);

        final var order = inOrder(request, dispatcher);
        order.verify(request).setAttribute("id", "gugu");
        order.verify(dispatcher).forward(request, response);
        verify(request).setAttribute("count", 2);
        verifyNoInteractions(response);
    }

    @Test
    void redirectsWithoutForwarding() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        new JspView("redirect:/users").render(Map.of("id", "gugu"), request, response);

        verify(response).sendRedirect("/users");
        verifyNoInteractions(request);
    }

    @Test
    void forwardsWithEmptyModelWithoutChangingAttributes() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/users.jsp")).thenReturn(dispatcher);

        new JspView("/users.jsp").render(Map.of(), request, response);

        verify(request).getRequestDispatcher("/users.jsp");
        verifyNoMoreInteractions(request);
        verify(dispatcher).forward(request, response);
    }
}
