package com.interface21.webmvc.servlet.view;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.junit.jupiter.api.Test;

class JspViewTest {

    @Test
    void forwardsAfterSettingModelAttributes() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/WEB-INF/views/user.jsp")).thenReturn(dispatcher);
        final var model = Map.of("id", "gugu", "age", 20);

        new JspView("/WEB-INF/views/user.jsp").render(model, request, response);

        final var ordered = inOrder(request, dispatcher);
        for (var entry : model.entrySet()) {
            ordered.verify(request).setAttribute(entry.getKey(), entry.getValue());
        }
        ordered.verify(request).getRequestDispatcher("/WEB-INF/views/user.jsp");
        ordered.verify(dispatcher).forward(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    void redirectsWithoutForwarding() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);

        new JspView("redirect:/login").render(Map.of("id", "gugu"), request, response);

        verify(response).sendRedirect("/login");
        verify(request, never()).getRequestDispatcher(anyString());
        verifyNoInteractions(dispatcher);
    }
}
