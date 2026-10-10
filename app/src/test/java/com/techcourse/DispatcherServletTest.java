package com.techcourse;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class DispatcherServletTest {

    private DispatcherServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        servlet = new DispatcherServlet();
        servlet.init();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        when(request.getMethod()).thenReturn("GET");
        when(request.getSession()).thenReturn(mock(HttpSession.class));
    }

    @Test
    void rendersLegacyController() throws Exception {
        when(request.getRequestURI()).thenReturn("/login/view");
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/login.jsp")).thenReturn(dispatcher);

        servlet.service(request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    void rendersAnnotatedControllerWithModel() throws Exception {
        when(request.getRequestURI()).thenReturn("/integration");
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/integration.jsp")).thenReturn(dispatcher);

        servlet.service(request, response);

        verify(request).setAttribute("message", "hello");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void prefersAnnotationMappingWhenBothMatch() throws Exception {
        when(request.getRequestURI()).thenReturn("/logout");

        servlet.service(request, response);

        verify(response).sendRedirect("/integration");
    }

    @Test
    void returnsNotFoundForUnmappedRequest() throws Exception {
        when(request.getRequestURI()).thenReturn("/missing");

        servlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void returnsNotFoundForUnmappedHttpMethod() throws Exception {
        when(request.getRequestURI()).thenReturn("/integration");
        when(request.getMethod()).thenReturn("POST");

        servlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
    }
}
