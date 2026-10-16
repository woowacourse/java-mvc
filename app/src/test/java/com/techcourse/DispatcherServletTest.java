package com.techcourse;

import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.DispatcherServlet;
import com.techcourse.controller.UserSession;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.Servlet;
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

    private Servlet servlet;

    @BeforeEach
    void setUp() {
        final DispatcherServlet dispatcherServlet = new DispatcherServlet("com.techcourse.controller");
        dispatcherServlet.init();
        servlet = dispatcherServlet;
    }

    @Test
    void serviceForwardsHomeRequest() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/");
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(requestDispatcher);

        servlet.service(request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void serviceLogsOutAndRedirectsHome() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final HttpSession session = mock(HttpSession.class);

        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/logout");
        when(request.getSession()).thenReturn(session);

        servlet.service(request, response);

        verify(session).removeAttribute(UserSession.SESSION_KEY);
        verify(response).sendRedirect("/");
    }

    @Test
    void serviceForwardsLoginViewForGuest() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final HttpSession session = mock(HttpSession.class);
        final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/login/view");
        when(request.getSession()).thenReturn(session);
        when(request.getRequestDispatcher("/login.jsp")).thenReturn(requestDispatcher);

        servlet.service(request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void serviceRedirectsLoginViewForLoggedInUser() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final HttpSession session = mock(HttpSession.class);
        final User user = new User(1, "gugu", "password", "gugu@example.com");

        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/login/view");
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(UserSession.SESSION_KEY)).thenReturn(user);

        servlet.service(request, response);

        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    void serviceLogsInAndRedirectsHome() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final HttpSession session = mock(HttpSession.class);
        final User user = InMemoryUserRepository.findByAccount("gugu").orElseThrow();

        when(request.getMethod()).thenReturn("POST");
        when(request.getRequestURI()).thenReturn("/login");
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("account")).thenReturn("gugu");
        when(request.getParameter("password")).thenReturn("password");

        servlet.service(request, response);

        verify(session).setAttribute(UserSession.SESSION_KEY, user);
        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    void serviceRedirectsAlreadyLoggedInUser() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final HttpSession session = mock(HttpSession.class);
        final User user = InMemoryUserRepository.findByAccount("gugu").orElseThrow();

        when(request.getMethod()).thenReturn("POST");
        when(request.getRequestURI()).thenReturn("/login");
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(UserSession.SESSION_KEY)).thenReturn(user);

        servlet.service(request, response);

        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    void serviceRedirectsWhenLoginPasswordIsWrong() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final HttpSession session = mock(HttpSession.class);

        when(request.getMethod()).thenReturn("POST");
        when(request.getRequestURI()).thenReturn("/login");
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("account")).thenReturn("gugu");
        when(request.getParameter("password")).thenReturn("wrong-password");

        servlet.service(request, response);

        verify(response).sendRedirect("/401.jsp");
    }

    @Test
    void serviceRedirectsWhenLoginAccountIsUnknown() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final HttpSession session = mock(HttpSession.class);

        when(request.getMethod()).thenReturn("POST");
        when(request.getRequestURI()).thenReturn("/login");
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("account")).thenReturn("unknown-user");

        servlet.service(request, response);

        verify(response).sendRedirect("/401.jsp");
    }

    @Test
    void serviceRedirectsAfterAnnotatedRegisterRequest() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getMethod()).thenReturn("POST");
        when(request.getRequestURI()).thenReturn("/register");
        when(request.getParameter("account")).thenReturn("step2-user");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getParameter("email")).thenReturn("step2@example.com");

        servlet.service(request, response);

        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    void serviceForwardsAnnotatedRegisterViewRequest() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/register");
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(requestDispatcher);

        servlet.service(request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void serviceForwardsRegisterViewAliasRequest() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/register/view");
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(requestDispatcher);

        servlet.service(request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void serviceRendersUserAsJson() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final StringWriter body = new StringWriter();

        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/api/user");
        when(request.getParameter("account")).thenReturn("gugu");
        when(response.getWriter()).thenReturn(new PrintWriter(body));

        servlet.service(request, response);

        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(body.toString()).isEqualTo("{\"account\":\"gugu\"}");
    }

    @Test
    void serviceRespondsNotFoundForUnsupportedHttpMethod() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getMethod()).thenReturn("CONNECT");
        when(request.getRequestURI()).thenReturn("/");

        servlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
    }
}
