package com.techcourse.controller;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.techcourse.domain.User;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LegacyControllerMigrationTest {

    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        handlerMapping = new AnnotationHandlerMapping("com.techcourse.controller");
        handlerMapping.initialize();
    }

    @Test
    void showsLoginPageForAnonymousUser() throws Exception {
        final var request = mockRequest("GET", "/login");
        final var response = mock(HttpServletResponse.class);
        final var session = mock(HttpSession.class);
        final var requestDispatcher = mock(RequestDispatcher.class);
        when(request.getSession()).thenReturn(session);
        when(request.getRequestDispatcher("/login.jsp")).thenReturn(requestDispatcher);

        final ModelAndView modelAndView = handle(request, response);
        modelAndView.getView().render(modelAndView.getModel(), request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void redirectsLoggedInUserFromLoginPage() throws Exception {
        final var request = mockRequest("GET", "/login");
        final var response = mock(HttpServletResponse.class);
        final var session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(UserSession.SESSION_KEY))
                .thenReturn(new User(1L, "gugu", "password", "gugu@example.com"));

        final ModelAndView modelAndView = handle(request, response);
        modelAndView.getView().render(modelAndView.getModel(), request, response);

        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    void logsInWithValidCredentials() throws Exception {
        final var request = mockRequest("POST", "/login");
        final var response = mock(HttpServletResponse.class);
        final var session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("account")).thenReturn("gugu");
        when(request.getParameter("password")).thenReturn("password");

        final ModelAndView modelAndView = handle(request, response);
        modelAndView.getView().render(modelAndView.getModel(), request, response);

        verify(session).setAttribute(any(String.class), any(User.class));
        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    void rejectsInvalidCredentials() throws Exception {
        final var request = mockRequest("POST", "/login");
        final var response = mock(HttpServletResponse.class);
        final var session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("account")).thenReturn("gugu");
        when(request.getParameter("password")).thenReturn("wrong-password");

        final ModelAndView modelAndView = handle(request, response);
        modelAndView.getView().render(modelAndView.getModel(), request, response);

        verify(session, never()).setAttribute(any(String.class), any());
        verify(response).sendRedirect("/401.jsp");
    }

    @Test
    void logsOutAndRedirectsToHome() throws Exception {
        final var request = mockRequest("POST", "/logout");
        final var response = mock(HttpServletResponse.class);
        final var session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);

        final ModelAndView modelAndView = handle(request, response);
        modelAndView.getView().render(modelAndView.getModel(), request, response);

        verify(session).removeAttribute(UserSession.SESSION_KEY);
        verify(response).sendRedirect("/");
    }

    @Test
    void showsHomePage() throws Exception {
        final var request = mockRequest("GET", "/");
        final var response = mock(HttpServletResponse.class);
        final var requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(requestDispatcher);

        final ModelAndView modelAndView = handle(request, response);
        modelAndView.getView().render(modelAndView.getModel(), request, response);

        verify(requestDispatcher).forward(request, response);
    }

    private ModelAndView handle(
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws Exception {
        final var handler = (HandlerExecution) handlerMapping.getHandler(request);
        return handler.handle(request, response);
    }

    private static HttpServletRequest mockRequest(final String method, final String uri) {
        final var request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn(method);
        when(request.getRequestURI()).thenReturn(uri);
        return request;
    }
}
