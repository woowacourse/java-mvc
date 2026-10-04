package com.interface21.webmvc.servlet.view;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

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
    void renderForwardsModelToJsp() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/profile.jsp")).thenReturn(dispatcher);

        new JspView("/profile.jsp").render(Map.of("name", "gugu"), request, response);

        final InOrder inOrder = inOrder(request, dispatcher);
        inOrder.verify(request).setAttribute("name", "gugu");
        inOrder.verify(request).getRequestDispatcher("/profile.jsp");
        inOrder.verify(dispatcher).forward(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    void renderRedirectsWithoutForwarding() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        new JspView("redirect:/index.jsp").render(Map.of("name", "gugu"), request, response);

        verify(response).sendRedirect("/index.jsp");
        verifyNoInteractions(request);
    }
}
