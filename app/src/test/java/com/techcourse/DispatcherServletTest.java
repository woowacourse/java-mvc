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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DispatcherServletTest {

    private final DispatcherServlet servlet = new DispatcherServlet();
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);

    @BeforeEach
    void setUp() {
        servlet.init();
    }

    @Test
    void homeControllerForwardsToJsp() throws Exception {
        request("GET", "/");
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(dispatcher);

        servlet.service(request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    void loginControllerLogsInAndRedirects() throws Exception {
        request("POST", "/login");
        final var session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("account")).thenReturn("gugu");
        when(request.getParameter("password")).thenReturn("password");

        servlet.service(request, response);

        final var user = InMemoryUserRepository.findByAccount("gugu").orElseThrow();
        verify(session).setAttribute(UserSession.SESSION_KEY, user);
        verify(response).sendRedirect("/index.jsp");
        verify(request, never()).getRequestDispatcher(anyString());
    }

    @Test
    void logoutControllerRemovesUserAndRedirects() throws Exception {
        request("GET", "/logout");
        final var session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);

        servlet.service(request, response);

        verify(session).removeAttribute(UserSession.SESSION_KEY);
        verify(response).sendRedirect("/");
    }

    @Test
    void registerViewControllerShowsForm() throws Exception {
        request("GET", "/register/view");
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(dispatcher);

        servlet.service(request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    void annotationControllerShowsRegisterForm() throws Exception {
        request("GET", "/register");
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(dispatcher);

        servlet.service(request, response);

        verify(dispatcher).forward(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    void annotationControllerRegistersUser() throws Exception {
        request("POST", "/register");
        when(request.getParameter("account")).thenReturn("step2-user");
        when(request.getParameter("password")).thenReturn("test-password");
        when(request.getParameter("email")).thenReturn("step2@example.com");

        servlet.service(request, response);

        final var user = InMemoryUserRepository.findByAccount("step2-user");
        assertThat(user).isPresent();
        assertThat(user.orElseThrow().checkPassword("test-password")).isTrue();
        verify(response).sendRedirect("/index.jsp");
        verify(request, never()).getRequestDispatcher(anyString());
    }

    @Test
    void unsupportedMethodReturnsNotFound() throws Exception {
        request("PUT", "/register");

        servlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    void unknownHttpMethodReturnsNotFound() throws Exception {
        request("UPDATE", "/register");

        servlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
        verify(response, never()).sendRedirect(anyString());
        verify(request, never()).getRequestDispatcher(anyString());
    }

    @Test
    void unknownUriReturnsNotFound() throws Exception {
        request("GET", "/not-registered");

        servlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void wrongPasswordRedirectsToUnauthorizedPage() throws Exception {
        request("POST", "/login");
        final var session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("account")).thenReturn("gugu");
        when(request.getParameter("password")).thenReturn("wrong");

        servlet.service(request, response);

        verify(response).sendRedirect("/401.jsp");
        verify(session, never()).setAttribute(anyString(), org.mockito.ArgumentMatchers.any());
    }

    @Test
    void missingAccountRedirectsToUnauthorizedPage() throws Exception {
        request("POST", "/login");
        when(request.getSession()).thenReturn(mock(HttpSession.class));
        when(request.getParameter("account")).thenReturn("not-registered");

        servlet.service(request, response);

        verify(response).sendRedirect("/401.jsp");
    }

    @Test
    void loginViewForwardsWhenNotLoggedIn() throws Exception {
        request("GET", "/login/view");
        when(request.getSession()).thenReturn(mock(HttpSession.class));
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/login.jsp")).thenReturn(dispatcher);

        servlet.service(request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    void loggedInUserSkipsLogin() throws Exception {
        request("POST", "/login");
        final var session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(UserSession.SESSION_KEY))
                .thenReturn(InMemoryUserRepository.findByAccount("gugu").orElseThrow());

        servlet.service(request, response);

        verify(response).sendRedirect("/index.jsp");
    }

    private void request(final String method, final String uri) {
        when(request.getMethod()).thenReturn(method);
        when(request.getRequestURI()).thenReturn(uri);
    }
}
