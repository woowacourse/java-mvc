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
    void forwardsToJspAfterExposingModelAsRequestAttributes() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/users.jsp")).thenReturn(dispatcher);

        new JspView("/users.jsp").render(Map.of("id", "gugu"), request, response);

        final var order = inOrder(request, dispatcher);
        order.verify(request).setAttribute("id", "gugu");
        order.verify(request).getRequestDispatcher("/users.jsp");
        order.verify(dispatcher).forward(request, response);
        verifyNoInteractions(response);
    }

    @Test
    void redirectsWithoutForwardingOrExposingModel() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        new JspView("redirect:/index.jsp").render(Map.of("id", "gugu"), request, response);

        verify(response).sendRedirect("/index.jsp");
        verifyNoInteractions(request);
    }
}
