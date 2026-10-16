package com.interface21.webmvc.servlet;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

class DispatcherServletPackageScanTest {

    @Test
    void dispatchesToControllerInConfiguredPackage() throws Exception {
        final var dispatcherServlet = new DispatcherServlet("samples");
        dispatcherServlet.init();

        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var requestDispatcher = mock(RequestDispatcher.class);
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestDispatcher("")).thenReturn(requestDispatcher);

        dispatcherServlet.service(request, response);

        verify(request).setAttribute("id", "gugu");
        verify(requestDispatcher).forward(request, response);
    }
}
