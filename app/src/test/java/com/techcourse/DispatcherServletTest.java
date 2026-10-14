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
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        servlet = new DispatcherServlet();
        servlet.init();
        response = mock(HttpServletResponse.class);
    }

    @Test
    void forwardsLegacyControllerResult() throws Exception {
        final var request = request("/", "GET");
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(dispatcher);

        servlet.service(request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    void redirectsLegacyControllerResult() throws Exception {
        final var request = request("/logout", "GET");
        when(request.getSession()).thenReturn(mock(HttpSession.class));

        servlet.service(request, response);

        verify(response).sendRedirect("/");
    }

    @Test
    void rendersAnnotatedControllerModel() throws Exception {
        final var request = request("/annotated", "GET");
        when(request.getParameter("id")).thenReturn("gugu");
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/annotated.jsp")).thenReturn(dispatcher);

        servlet.service(request, response);

        verify(request).setAttribute("id", "gugu");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void prioritizesAnnotatedMappingOverLegacyMapping() throws Exception {
        final var request = request("/login/view", "GET");
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/annotated.jsp")).thenReturn(dispatcher);

        servlet.service(request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    void fallsBackToLegacyWhenAnnotatedHttpMethodDoesNotMatch() throws Exception {
        final var request = request("/login/view", "POST");
        when(request.getSession()).thenReturn(mock(HttpSession.class));
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/login.jsp")).thenReturn(dispatcher);

        servlet.service(request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    void respondsWithNotFoundWhenNoMappingMatches() throws Exception {
        final var request = request("/unknown", "GET");

        servlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void prioritizesExplicitHttpMethodOverDefaultMapping() throws Exception {
        final var request = request("/method-selection", "GET");
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/specific.jsp")).thenReturn(dispatcher);

        servlet.service(request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    void usesDefaultMappingWhenNoExplicitHttpMethodMatches() throws Exception {
        final var request = request("/method-selection", "POST");
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/default.jsp")).thenReturn(dispatcher);

        servlet.service(request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    void preservesControllerFailureCause() {
        final var request = request("/annotated-error", "GET");

        assertThatThrownBy(() -> servlet.service(request, response))
                .isInstanceOf(ServletException.class)
                .hasRootCauseInstanceOf(IllegalStateException.class)
                .hasRootCauseMessage("controller failure");
    }

    private HttpServletRequest request(final String uri, final String method) {
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn(uri);
        when(request.getMethod()).thenReturn(method);
        return request;
    }
}
