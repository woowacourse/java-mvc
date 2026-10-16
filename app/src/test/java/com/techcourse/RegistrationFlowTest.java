package com.techcourse;

import com.techcourse.controller.UserSession;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RegistrationFlowTest {

    private DispatcherServlet servlet;
    private String account;

    @BeforeEach
    void setUp() {
        servlet = new DispatcherServlet();
        servlet.init();
        account = "step2-" + UUID.randomUUID();
    }

    @Test
    void showsRegistrationFormWithGet() throws Exception {
        final var request = request("/register", "GET");
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(dispatcher);

        servlet.service(request, response);

        verify(dispatcher).forward(request, response);
        assertThat(InMemoryUserRepository.findByAccount(account)).isEmpty();
    }

    @Test
    void savesMemberAndRedirectsWithPost() throws Exception {
        final var request = registrationRequest("POST");
        final var response = mock(HttpServletResponse.class);

        servlet.service(request, response);

        assertThat(InMemoryUserRepository.findByAccount(account))
                .hasValueSatisfying(user -> assertThat(user.checkPassword("password")).isTrue());
        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    void preservesRegistrationFormUrl() throws Exception {
        final var request = request("/register/view", "GET");
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(dispatcher);

        servlet.service(request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    void registeredMemberCanLoginAndLogout() throws Exception {
        servlet.service(registrationRequest("POST"), mock(HttpServletResponse.class));
        final var user = InMemoryUserRepository.findByAccount(account).orElseThrow();
        final var session = mock(HttpSession.class);
        final var loginRequest = request("/login", "POST");
        when(loginRequest.getSession()).thenReturn(session);
        when(loginRequest.getParameter("account")).thenReturn(account);
        when(loginRequest.getParameter("password")).thenReturn("password");
        final var loginResponse = mock(HttpServletResponse.class);

        servlet.service(loginRequest, loginResponse);

        verify(session).setAttribute(UserSession.SESSION_KEY, user);
        verify(loginResponse).sendRedirect("/index.jsp");

        final var logoutRequest = request("/logout", "GET");
        when(logoutRequest.getSession()).thenReturn(session);
        final var logoutResponse = mock(HttpServletResponse.class);

        servlet.service(logoutRequest, logoutResponse);

        verify(session).removeAttribute(UserSession.SESSION_KEY);
        verify(logoutResponse).sendRedirect("/");
    }

    @Test
    void doesNotSaveMemberForUnmappedHttpMethod() throws Exception {
        final var request = registrationRequest("PUT");
        final var response = mock(HttpServletResponse.class);

        servlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
        assertThat(InMemoryUserRepository.findByAccount(account)).isEmpty();
    }

    private HttpServletRequest registrationRequest(final String method) {
        final var request = request("/register", method);
        when(request.getParameter("account")).thenReturn(account);
        when(request.getParameter("password")).thenReturn("password");
        when(request.getParameter("email")).thenReturn("step2@example.com");
        return request;
    }

    private HttpServletRequest request(final String uri, final String method) {
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn(uri);
        when(request.getMethod()).thenReturn(method);
        return request;
    }
}
