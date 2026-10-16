package com.techcourse;

import com.interface21.web.http.MediaType;
import com.techcourse.controller.UserSession;
import jakarta.servlet.RequestDispatcher;
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

    private DispatcherServlet servlet;

    @BeforeEach
    void setUp() {
        servlet = new DispatcherServlet();
        servlet.init();
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
}
