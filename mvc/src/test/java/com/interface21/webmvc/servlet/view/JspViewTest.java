package com.interface21.webmvc.servlet.view;

import static org.mockito.ArgumentMatchers.anyString;
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
    void redirect() throws Exception {
        //given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        //when
        new JspView("redirect:/index.jsp").render(Map.of(), request, response);

        //then
        verify(response).sendRedirect("/index.jsp");
        verify(request, never()).getRequestDispatcher(anyString());
    }

    @Test
    void forward() throws Exception {
        //given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);

        when(request.getRequestDispatcher("/login.jsp")).thenReturn(dispatcher);

        //when
        new JspView("/login.jsp").render(Map.of(), request, response);

        //then
        verify(dispatcher).forward(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    void forwardWithModel() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(request.getRequestDispatcher("/login.jsp")).thenReturn(mock(RequestDispatcher.class));

        new JspView("/login.jsp").render(Map.of("user", "gugu"), request, response);

        verify(request).setAttribute("user", "gugu");
    }
}
