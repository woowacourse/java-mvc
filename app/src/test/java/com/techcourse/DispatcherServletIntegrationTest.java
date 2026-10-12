package com.techcourse;

import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.DispatcherServlet;
import com.techcourse.controller.UserSession;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class DispatcherServletIntegrationTest {

    private DispatcherServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;

    @BeforeEach
    void setUp() {
        servlet = new DispatcherServlet("com.techcourse.controller");
        servlet.init();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
    }

    @Test
    void existingPageRoutesStillForwardToTheirJsps() throws Exception {
        final MapEntry[] pages = {
                new MapEntry("/", "/index.jsp"),
                new MapEntry("/login/view", "/login.jsp"),
                new MapEntry("/register/view", "/register.jsp"),
                new MapEntry("/register", "/register.jsp")
        };
        for (final MapEntry page : pages) {
            final RequestDispatcher dispatcher = mock(RequestDispatcher.class);
            when(request.getRequestDispatcher(page.jsp())).thenReturn(dispatcher);
            dispatch("GET", page.path());
            verify(dispatcher).forward(request, response);
        }
    }

    @Test
    void successfulLoginStoresUserAndRedirects() throws Exception {
        when(request.getParameter("account")).thenReturn("gugu");
        when(request.getParameter("password")).thenReturn("password");

        dispatch("POST", "/login");

        verify(session).setAttribute(eq(UserSession.SESSION_KEY), any(User.class));
        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    void incorrectPasswordRedirectsWithoutLoggingIn() throws Exception {
        when(request.getParameter("account")).thenReturn("gugu");
        when(request.getParameter("password")).thenReturn("wrong");

        dispatch("POST", "/login");

        verify(session, never()).setAttribute(eq(UserSession.SESSION_KEY), any());
        verify(response).sendRedirect("/401.jsp");
    }

    @Test
    void unknownAccountCannotLogIn() throws Exception {
        when(request.getParameter("account")).thenReturn("unknown-step3-account");
        dispatch("POST", "/login");
        verify(response).sendRedirect("/401.jsp");
    }

    @Test
    void loggedInUserIsRedirectedFromLoginPage() throws Exception {
        when(session.getAttribute(UserSession.SESSION_KEY))
                .thenReturn(InMemoryUserRepository.findByAccount("gugu").orElseThrow());
        dispatch("GET", "/login/view");
        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    void logoutClearsSessionAndRedirects() throws Exception {
        dispatch("GET", "/logout");
        verify(session).removeAttribute(UserSession.SESSION_KEY);
        verify(response).sendRedirect("/");
    }

    @Test
    void registrationSavesUserAndRedirects() throws Exception {
        when(request.getParameter("account")).thenReturn("step3-registration");
        when(request.getParameter("password")).thenReturn("secret");
        when(request.getParameter("email")).thenReturn("step3@example.com");

        dispatch("POST", "/register");

        assertThat(InMemoryUserRepository.findByAccount("step3-registration")).isPresent();
        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    void userApiWritesJsonThroughTheCommonRenderingFlow() throws Exception {
        final StringWriter body = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(body));
        when(request.getParameter("account")).thenReturn("gugu");

        dispatch("GET", "/api/user");

        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(body.toString()).isEqualTo("{\"account\":\"gugu\"}");
    }

    @Test
    void unknownRouteReturnsNotFound() throws Exception {
        dispatch("GET", "/missing");
        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void getRequestDoesNotExecuteLoginAction() throws Exception {
        dispatch("GET", "/login");
        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
        verifyNoInteractions(session);
    }

    private void dispatch(final String method, final String path) throws Exception {
        when(request.getMethod()).thenReturn(method);
        when(request.getRequestURI()).thenReturn(path);
        servlet.service((ServletRequest) request, (ServletResponse) response);
    }

    private record MapEntry(String path, String jsp) {
    }
}
