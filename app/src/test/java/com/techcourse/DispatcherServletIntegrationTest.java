package com.techcourse;

import com.interface21.webmvc.servlet.mvc.asis.SimpleControllerHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecutionHandlerAdapter;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DispatcherServletIntegrationTest {

    private DispatcherServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        servlet = new DispatcherServlet(
                List.of(new AnnotationHandlerMapping("fixtures.dispatcher"), new ManualHandlerMapping()),
                List.of(new SimpleControllerHandlerAdapter(), new HandlerExecutionHandlerAdapter()));
        servlet.init();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        when(request.getMethod()).thenReturn("GET");
    }

    @Test
    void rendersAnnotatedControllerViewWithModel() throws Exception {
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestURI()).thenReturn("/annotation");
        when(request.getParameter("name")).thenReturn("gugu");
        when(request.getRequestDispatcher("/annotation.jsp")).thenReturn(dispatcher);

        servlet.service(request, response);

        verify(request).setAttribute("name", "gugu");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void rendersAnnotatedControllerRedirect() throws Exception {
        when(request.getRequestURI()).thenReturn("/annotation-redirect");
        when(request.getMethod()).thenReturn("POST");

        servlet.service(request, response);

        verify(response).sendRedirect("/annotation");
        verify(request, never()).getRequestDispatcher(anyString());
    }

    @Test
    void fallsBackToLegacyMappingWhenAnnotationMappingHasNoHandler() throws Exception {
        when(request.getRequestURI()).thenReturn("/logout");
        final var session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);

        servlet.service(request, response);

        verify(session).removeAttribute("user");
        verify(response).sendRedirect("/");
    }

    @Test
    void usesFirstMatchingHandlerMapping() throws Exception {
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestURI()).thenReturn("/");
        when(request.getRequestDispatcher("/annotation-home.jsp")).thenReturn(dispatcher);

        servlet.service(request, response);

        verify(dispatcher).forward(request, response);
        verify(request, never()).getRequestDispatcher("/index.jsp");
    }

    @Test
    void sendsNotFoundForUnmappedPath() throws Exception {
        when(request.getRequestURI()).thenReturn("/missing");

        servlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void sendsNotFoundForUnmappedHttpMethod() throws Exception {
        when(request.getRequestURI()).thenReturn("/annotation");
        when(request.getMethod()).thenReturn("DELETE");

        servlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void reportsMissingHandlerAdapter() {
        servlet = new DispatcherServlet(List.of(new AnnotationHandlerMapping("fixtures.dispatcher")), List.of());
        servlet.init();
        when(request.getRequestURI()).thenReturn("/annotation");

        assertThatThrownBy(() -> servlet.service(request, response))
                .isInstanceOf(ServletException.class)
                .hasCauseInstanceOf(IllegalStateException.class)
                .hasMessageContaining("No handler adapter");
    }

    @Test
    void preservesControllerFailureCause() {
        when(request.getRequestURI()).thenReturn("/annotation-failure");

        assertThatThrownBy(() -> servlet.service(request, response))
                .isInstanceOf(ServletException.class)
                .hasRootCauseInstanceOf(IllegalStateException.class)
                .hasRootCauseMessage("controller failed");
    }

    @Test
    void preservesViewRenderingFailureCause() throws Exception {
        final var dispatcher = mock(RequestDispatcher.class);
        final var failure = new IOException("forward failed");
        when(request.getRequestURI()).thenReturn("/annotation");
        when(request.getRequestDispatcher("/annotation.jsp")).thenReturn(dispatcher);
        doThrow(failure).when(dispatcher).forward(request, response);

        assertThatThrownBy(() -> servlet.service(request, response))
                .isInstanceOf(ServletException.class)
                .hasCause(failure);
    }
}
