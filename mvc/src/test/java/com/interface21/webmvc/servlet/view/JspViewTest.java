package com.interface21.webmvc.servlet.view;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class JspViewTest {

    @Test
    void forwardsToJspAfterSettingModelAttributes() throws Exception {
        final var view = new JspView("/user.jsp");
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/user.jsp")).thenReturn(requestDispatcher);

        view.render(Map.of("id", "gugu"), request, response);

        final var order = inOrder(request, requestDispatcher);
        order.verify(request).setAttribute("id", "gugu");
        order.verify(request).getRequestDispatcher("/user.jsp");
        order.verify(requestDispatcher).forward(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    void redirectsWithoutForwarding() throws Exception {
        final var view = new JspView("redirect:/login");
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        view.render(Map.of("id", "gugu"), request, response);

        verify(response).sendRedirect("/login");
        verifyNoInteractions(request);
    }
}
