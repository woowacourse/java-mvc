package com.interface21.webmvc.servlet;

import com.techcourse.controller.UserSession;
import com.techcourse.domain.User;
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

class DispatcherServletTest {

    private DispatcherServlet dispatcherServlet;

    @BeforeEach
    void setUp() {
        dispatcherServlet = new DispatcherServlet("com.techcourse.controller");
        dispatcherServlet.init();
    }

    @Test
    void service_RegisterViewRequest_ForwardsToJsp() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/register/view");
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(requestDispatcher);

        dispatcherServlet.service(request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void service_AnnotatedRegisterRequest_SavesUserAndRedirects() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        String account = "register-test-" + UUID.randomUUID();
        when(request.getMethod()).thenReturn("POST");
        when(request.getRequestURI()).thenReturn("/register");
        when(request.getParameter("account")).thenReturn(account);
        when(request.getParameter("password")).thenReturn("password");
        when(request.getParameter("email")).thenReturn("test@example.com");

        dispatcherServlet.service(request, response);

        assertThat(InMemoryUserRepository.findByAccount(account)).isPresent();
        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    void service_HomeRequest_ForwardsToIndexJsp() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/");
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(requestDispatcher);

        dispatcherServlet.service(request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void service_AnonymousLoginViewRequest_ForwardsToLoginJsp() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/login/view");
        when(request.getSession()).thenReturn(session);
        when(request.getRequestDispatcher("/login.jsp")).thenReturn(requestDispatcher);

        dispatcherServlet.service(request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void service_ValidLoginRequest_SavesUserInSessionAndRedirects() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        String account = "login-test-" + UUID.randomUUID();
        User user = new User(3, account, "password", "test@example.com");
        InMemoryUserRepository.save(user);
        when(request.getMethod()).thenReturn("POST");
        when(request.getRequestURI()).thenReturn("/login");
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("account")).thenReturn(account);
        when(request.getParameter("password")).thenReturn("password");

        dispatcherServlet.service(request, response);

        verify(session).setAttribute(UserSession.SESSION_KEY, user);
        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    void service_WrongPasswordLoginRequest_RedirectsToUnauthorizedPage() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        String account = "login-test-" + UUID.randomUUID();
        InMemoryUserRepository.save(new User(4, account, "password", "test@example.com"));
        when(request.getMethod()).thenReturn("POST");
        when(request.getRequestURI()).thenReturn("/login");
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("account")).thenReturn(account);
        when(request.getParameter("password")).thenReturn("wrong-password");

        dispatcherServlet.service(request, response);

        verify(response).sendRedirect("/401.jsp");
    }

    @Test
    void service_LogoutRequest_RemovesUserFromSessionAndRedirects() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/logout");
        when(request.getSession()).thenReturn(session);

        dispatcherServlet.service(request, response);

        verify(session).removeAttribute(UserSession.SESSION_KEY);
        verify(response).sendRedirect("/");
    }

    @Test
    void service_UnknownPath_SendsNotFound() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/nope");

        dispatcherServlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void service_UnsupportedMethod_SendsMethodNotAllowed() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/register");

        dispatcherServlet.service(request, response);

        verify(response).setHeader("Allow", "POST");
        verify(response).sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }
}
