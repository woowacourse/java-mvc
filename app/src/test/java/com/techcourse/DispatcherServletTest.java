package com.techcourse;

import com.techcourse.controller.UserSession;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DispatcherServletTest {

    private DispatcherServlet servlet;

    @BeforeEach
    void setUp() {
        servlet = new DispatcherServlet();
        servlet.init();
    }

    @Test
    void showsLegacyLoginPage() throws Exception {
        final var request = request("GET", "/login/view");
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getSession()).thenReturn(mock(HttpSession.class));
        when(request.getRequestDispatcher("/login.jsp")).thenReturn(dispatcher);

        servlet.service(request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    void showsAnnotatedRegistrationPage() throws Exception {
        final var request = request("GET", "/register");
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(dispatcher);

        servlet.service(request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    void registersWithAnnotationAndLogsInWithLegacyController() throws Exception {
        final var registerRequest = request("POST", "/register");
        final var registerResponse = mock(HttpServletResponse.class);
        when(registerRequest.getParameter("account")).thenReturn("step2-user");
        when(registerRequest.getParameter("password")).thenReturn("password");
        when(registerRequest.getParameter("email")).thenReturn("step2@example.com");

        servlet.service(registerRequest, registerResponse);

        verify(registerResponse).sendRedirect("/index.jsp");
        final var user = InMemoryUserRepository.findByAccount("step2-user").orElseThrow();
        assertThat(user.checkPassword("password")).isTrue();

        final var loginRequest = request("POST", "/login");
        final var loginResponse = mock(HttpServletResponse.class);
        final var session = mock(HttpSession.class);
        when(loginRequest.getSession()).thenReturn(session);
        when(loginRequest.getParameter("account")).thenReturn("step2-user");
        when(loginRequest.getParameter("password")).thenReturn("password");

        servlet.service(loginRequest, loginResponse);

        verify(session).setAttribute(UserSession.SESSION_KEY, user);
        verify(loginResponse).sendRedirect("/index.jsp");
    }

    @Test
    void returnsNotFoundWhenNoHandlerMatches() throws Exception {
        final var request = request("GET", "/unknown");
        final var response = mock(HttpServletResponse.class);

        servlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void doesNotFallBackToLegacyRegistrationForUnsupportedMethod() throws Exception {
        final var request = request("PUT", "/register");
        final var response = mock(HttpServletResponse.class);

        servlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    private HttpServletRequest request(final String method, final String uri) {
        final var request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn(method);
        when(request.getRequestURI()).thenReturn(uri);
        return request;
    }
}
