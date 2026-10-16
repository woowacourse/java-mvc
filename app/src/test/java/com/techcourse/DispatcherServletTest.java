package com.techcourse;

import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.DispatcherServlet;
import com.techcourse.controller.UserSession;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DispatcherServletTest {

    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final HttpSession session = mock(HttpSession.class);
    private final DispatcherServlet dispatcherServlet = new DispatcherServlet("com.techcourse");

    @BeforeEach
    void setUp() {
        dispatcherServlet.init();
        when(request.getSession()).thenReturn(session);
    }

    @Test
    void returnsNotFoundWhenHandlerDoesNotExist() throws Exception {
        service("/not-found", "GET");

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void showsHome() throws Exception {
        final var requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(requestDispatcher);

        service("/", "GET");

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void showsLogin() throws Exception {
        final var requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/login.jsp")).thenReturn(requestDispatcher);

        service("/login/view", "GET");

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void redirectsLoggedInUserFromLoginView() throws Exception {
        when(session.getAttribute(UserSession.SESSION_KEY))
                .thenReturn(InMemoryUserRepository.findByAccount("gugu").orElseThrow());

        service("/login/view", "GET");

        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    void logsInWithValidCredentials() throws Exception {
        final var user = InMemoryUserRepository.findByAccount("gugu").orElseThrow();
        when(request.getParameter("account")).thenReturn("gugu");
        when(request.getParameter("password")).thenReturn("password");

        service("/login", "POST");

        verify(session).setAttribute(UserSession.SESSION_KEY, user);
        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    void rejectsInvalidPassword() throws Exception {
        when(request.getParameter("account")).thenReturn("gugu");
        when(request.getParameter("password")).thenReturn("wrong");

        service("/login", "POST");

        verify(response).sendRedirect("/401.jsp");
    }

    @Test
    void rejectsUnknownAccount() throws Exception {
        when(request.getParameter("account")).thenReturn("unknown");

        service("/login", "POST");

        verify(response).sendRedirect("/401.jsp");
    }

    @Test
    void logsOut() throws Exception {
        service("/logout", "GET");

        verify(session).removeAttribute(UserSession.SESSION_KEY);
        verify(response).sendRedirect("/");
    }

    @Test
    void showsRegister() throws Exception {
        final var requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(requestDispatcher);

        service("/register", "GET");

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void registersUser() throws Exception {
        when(request.getParameter("account")).thenReturn("new-user");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getParameter("email")).thenReturn("user@example.com");

        service("/register", "POST");

        assertThat(InMemoryUserRepository.findByAccount("new-user")).isPresent();
        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    void returnsUserAsJson() throws Exception {
        final var body = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(body));
        when(request.getParameter("account")).thenReturn("gugu");

        service("/api/user", "GET");

        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(body.toString()).isEqualTo("{\"account\":\"gugu\"}");
    }

    private void service(final String uri, final String method) throws Exception {
        when(request.getRequestURI()).thenReturn(uri);
        when(request.getMethod()).thenReturn(method);
        dispatcherServlet.service((ServletRequest) request, response);
    }
}
