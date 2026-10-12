package com.interface21.webmvc.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DispatcherServletTest {

    @Test
    void handlesControllerFromConfiguredPackage() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");
        when(request.getAttribute("id")).thenReturn("configured-user");
        when(request.getRequestDispatcher("")).thenReturn(dispatcher);
        final var servlet = new DispatcherServlet("samples");
        servlet.init();

        servlet.service(request, response);

        verify(request).setAttribute("id", "configured-user");
        verify(dispatcher).forward(request, response);
        verify(response, never()).sendError(HttpServletResponse.SC_NOT_FOUND);
        verify(response, never()).sendRedirect(anyString());
    }
}
