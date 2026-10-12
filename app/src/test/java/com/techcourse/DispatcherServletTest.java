package com.techcourse;

import com.techcourse.controller.UserSession;
import com.interface21.webmvc.servlet.DispatcherServlet;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.io.StringWriter;
import java.io.PrintWriter;
import com.techcourse.domain.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DispatcherServletTest {

    private TestDispatcherServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        servlet = new TestDispatcherServlet();
        servlet.init();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        when(request.getMethod()).thenReturn("GET");
    }

    @Test
    void rendersUserAsJson() throws Exception {
        final var account = "json-" + UUID.randomUUID();
        InMemoryUserRepository.save(new User(3, account, "secret", "user@example.com"));
        final var output = new StringWriter();
        when(request.getRequestURI()).thenReturn("/api/user");
        when(request.getParameter("account")).thenReturn(account);
        when(response.getWriter()).thenReturn(new PrintWriter(output));

        servlet.service(request, response);

        verify(response).setContentType("application/json;charset=UTF-8");
        assertThat(output.toString()).isEqualTo("{\"account\":\"" + account + "\"}");
    }

    @Test
    void rejectsGetLogin() throws Exception {
        when(request.getRequestURI()).thenReturn("/login");
        servlet.service(request, response);
        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void rendersLoginFormForAnonymousUser() throws Exception {
        when(request.getRequestURI()).thenReturn("/login/view");
        when(request.getSession()).thenReturn(mock(HttpSession.class));
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/login.jsp")).thenReturn(dispatcher);
        servlet.service(request, response);
        verify(dispatcher).forward(request, response);
    }

    @Test
    void redirectsLoginFormForLoggedInUser() throws Exception {
        when(request.getRequestURI()).thenReturn("/login/view");
        final var session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(UserSession.SESSION_KEY)).thenReturn(
                InMemoryUserRepository.findByAccount("gugu").orElseThrow());
        servlet.service(request, response);
        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    void rejectsIncorrectPassword() throws Exception {
        login("gugu", "incorrect");
        verify(response).sendRedirect("/401.jsp");
    }

    @Test
    void rejectsUnknownAccount() throws Exception {
        login("missing-" + UUID.randomUUID(), "secret");
        verify(response).sendRedirect("/401.jsp");
    }

    @Test
    void redirectsAlreadyLoggedInUser() throws Exception {
        final var session = mock(HttpSession.class);
        when(session.getAttribute(UserSession.SESSION_KEY)).thenReturn(
                InMemoryUserRepository.findByAccount("gugu").orElseThrow());
        when(request.getSession()).thenReturn(session);
        when(request.getRequestURI()).thenReturn("/login");
        when(request.getMethod()).thenReturn("POST");
        servlet.service(request, response);
        verify(response).sendRedirect("/index.jsp");
    }

    private void login(final String account, final String password) throws Exception {
        when(request.getRequestURI()).thenReturn("/login");
        when(request.getMethod()).thenReturn("POST");
        when(request.getParameter("account")).thenReturn(account);
        when(request.getParameter("password")).thenReturn(password);
        when(request.getSession()).thenReturn(mock(HttpSession.class));
        servlet.service(request, response);
    }

    @Test
    void rendersRegistrationForm() throws Exception {
        when(request.getRequestURI()).thenReturn("/register");
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(dispatcher);

        servlet.service(request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    void registersUserThenLogsIn() throws Exception {
        final var account = "registration-" + UUID.randomUUID();
        when(request.getRequestURI()).thenReturn("/register");
        when(request.getMethod()).thenReturn("POST");
        when(request.getParameter("account")).thenReturn(account);
        when(request.getParameter("password")).thenReturn("secret");
        when(request.getParameter("email")).thenReturn("user@example.com");

        servlet.service(request, response);

        final var user = InMemoryUserRepository.findByAccount(account).orElseThrow();
        assertThat(user.checkPassword("secret")).isTrue();
        verify(response).sendRedirect("/index.jsp");

        final var loginRequest = mock(HttpServletRequest.class);
        final var loginResponse = mock(HttpServletResponse.class);
        final var session = mock(HttpSession.class);
        when(loginRequest.getRequestURI()).thenReturn("/login");
        when(loginRequest.getMethod()).thenReturn("POST");
        when(loginRequest.getParameter("account")).thenReturn(account);
        when(loginRequest.getParameter("password")).thenReturn("secret");
        when(loginRequest.getSession()).thenReturn(session);

        servlet.service(loginRequest, loginResponse);

        verify(session).setAttribute(UserSession.SESSION_KEY, user);
        verify(loginResponse).sendRedirect("/index.jsp");
    }

    @Test
    void returnsNotFoundForUnsupportedRegistrationMethod() throws Exception {
        when(request.getRequestURI()).thenReturn("/register");
        when(request.getMethod()).thenReturn("PUT");

        servlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void rendersHomeView() throws Exception {
        when(request.getRequestURI()).thenReturn("/");
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(dispatcher);

        servlet.service(request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    void redirectsLogoutController() throws Exception {
        when(request.getRequestURI()).thenReturn("/logout");
        final var session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);

        servlet.service(request, response);

        verify(session).removeAttribute(UserSession.SESSION_KEY);
        verify(response).sendRedirect("/");
    }

    @Test
    void rendersAnnotationViewWithModel() throws Exception {
        when(request.getRequestURI()).thenReturn("/adapter-test");
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/adapter.jsp")).thenReturn(dispatcher);

        servlet.service(request, response);

        verify(request).setAttribute("message", "hello");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void redirectsAnnotationController() throws Exception {
        when(request.getRequestURI()).thenReturn("/adapter-redirect");
        when(request.getMethod()).thenReturn("POST");

        servlet.service(request, response);

        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    void returnsNotFoundForMissingHandler() throws Exception {
        when(request.getRequestURI()).thenReturn("/missing");

        servlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void returnsNotFoundForUnmappedAnnotationMethod() throws Exception {
        when(request.getRequestURI()).thenReturn("/adapter-test");
        when(request.getMethod()).thenReturn("POST");

        servlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void preservesControllerExceptionAsCause() {
        when(request.getRequestURI()).thenReturn("/adapter-error");

        assertThatThrownBy(() -> servlet.service(request, response))
                .isInstanceOf(ServletException.class)
                .hasCauseInstanceOf(IllegalStateException.class)
                .hasRootCauseMessage("controller failure");
    }
    private static class TestDispatcherServlet extends DispatcherServlet {
        TestDispatcherServlet() {
            super("com.techcourse.controller");
        }

        @Override
        public void service(final HttpServletRequest request, final HttpServletResponse response)
                throws ServletException {
            super.service(request, response);
        }
    }
}
