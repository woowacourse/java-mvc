package com.techcourse;

import com.techcourse.controller.UserSession;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DispatcherServletTest {

    @Test
    void forwardsRootRequestToIndex() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestURI()).thenReturn("/");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(requestDispatcher);
        final var servlet = new DispatcherServlet();
        servlet.init();

        servlet.service(request, response);

        verify(requestDispatcher).forward(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    void removesUserSessionAndRedirectsLogoutRequestToRoot() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var session = mock(HttpSession.class);
        when(request.getRequestURI()).thenReturn("/logout");
        when(request.getMethod()).thenReturn("GET");
        when(request.getSession()).thenReturn(session);
        final var servlet = new DispatcherServlet();
        servlet.init();

        servlet.service(request, response);

        verify(session).removeAttribute(UserSession.SESSION_KEY);
        verify(response).sendRedirect("/");
        verify(request, never()).getRequestDispatcher(anyString());
    }

    @Test
    void handlesLegacyAndAnnotatedRequestsWithTheSameServlet() throws Exception {
        final var legacyRequest = mock(HttpServletRequest.class);
        final var legacyResponse = mock(HttpServletResponse.class);
        final var indexDispatcher = mock(RequestDispatcher.class);
        when(legacyRequest.getRequestURI()).thenReturn("/");
        when(legacyRequest.getMethod()).thenReturn("GET");
        when(legacyRequest.getRequestDispatcher("/index.jsp")).thenReturn(indexDispatcher);

        final var annotatedRequest = mock(HttpServletRequest.class);
        final var annotatedResponse = mock(HttpServletResponse.class);
        final var profileDispatcher = mock(RequestDispatcher.class);
        when(annotatedRequest.getRequestURI()).thenReturn("/annotation-test");
        when(annotatedRequest.getMethod()).thenReturn("GET");
        when(annotatedRequest.getParameter("id")).thenReturn("gugu");
        when(annotatedRequest.getRequestDispatcher("/profile.jsp")).thenReturn(profileDispatcher);

        final var servlet = new DispatcherServlet();
        servlet.init();

        servlet.service(legacyRequest, legacyResponse);
        servlet.service(annotatedRequest, annotatedResponse);

        verify(indexDispatcher).forward(legacyRequest, legacyResponse);
        verify(annotatedRequest).setAttribute("id", "gugu");
        verify(profileDispatcher).forward(annotatedRequest, annotatedResponse);
        verify(annotatedResponse, never()).sendRedirect(anyString());
    }

    @Test
    void selectsAnnotatedPostHandlerAndRedirects() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(request.getRequestURI()).thenReturn("/annotation-test");
        when(request.getMethod()).thenReturn("POST");
        final var servlet = new DispatcherServlet();
        servlet.init();

        servlet.service(request, response);

        verify(response).sendRedirect("/annotation-test");
        verify(request, never()).getRequestDispatcher(anyString());
    }

    @Test
    void returnsNotFoundWhenNoMappingMatches() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(request.getRequestURI()).thenReturn("/unregistered");
        when(request.getMethod()).thenReturn("GET");
        final var servlet = new DispatcherServlet();
        servlet.init();

        servlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
        verify(request, never()).getRequestDispatcher(anyString());
        verify(response, never()).sendRedirect(anyString());
    }
}
