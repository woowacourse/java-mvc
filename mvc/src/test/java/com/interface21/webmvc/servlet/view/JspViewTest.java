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
    void forwardsModelToJsp() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/user.jsp")).thenReturn(dispatcher);

        new JspView("/user.jsp").render(Map.of("id", "gugu"), request, response);

        verify(request).setAttribute("id", "gugu");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void redirectsWhenViewNameHasRedirectPrefix() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        new JspView("redirect:/index.jsp").render(Map.of(), request, response);

        verify(response).sendRedirect("/index.jsp");
        verify(request, never()).getRequestDispatcher(anyString());
    }
}
