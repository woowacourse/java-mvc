package com.techcourse;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    }

    @Test
    void rendersLegacyView() throws Exception {
        when(request.getRequestURI()).thenReturn("/");
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(dispatcher);

        servlet.service(request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    void redirectsLegacyController() throws Exception {
        when(request.getRequestURI()).thenReturn("/logout");
        when(request.getSession()).thenReturn(mock(HttpSession.class));

        servlet.service(request, response);

        verify(response).sendRedirect("/");
    }

    @Test
    void rendersAnnotationViewWithModel() throws Exception {
        when(request.getRequestURI()).thenReturn("/adapter-test");
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/adapter.jsp")).thenReturn(dispatcher);

        servlet.service(request, response);

        verify(request).setAttribute("message", "hello");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void redirectsAnnotationController() throws Exception {
        when(request.getRequestURI()).thenReturn("/adapter-redirect");
        when(request.getMethod()).thenReturn("POST");

        servlet.service(request, response);

        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    void returnsNotFoundForMissingHandler() throws Exception {
        when(request.getRequestURI()).thenReturn("/missing");

        servlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void returnsNotFoundForUnmappedAnnotationMethod() throws Exception {
        when(request.getRequestURI()).thenReturn("/adapter-test");
        when(request.getMethod()).thenReturn("POST");

        servlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void preservesControllerExceptionAsCause() {
        when(request.getRequestURI()).thenReturn("/adapter-error");

        assertThatThrownBy(() -> servlet.service(request, response))
                .isInstanceOf(ServletException.class)
                .hasCauseInstanceOf(IllegalStateException.class)
                .hasRootCauseMessage("controller failure");
    }
}
