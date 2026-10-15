package com.techcourse;

import com.techcourse.repository.InMemoryUserRepository;
import com.techcourse.domain.User;
import com.techcourse.controller.UserSession;
import com.interface21.webmvc.servlet.DispatcherServlet;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecutionAdapter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.io.PrintWriter;
import java.io.StringWriter;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DispatcherServletTest {

    private TestDispatcherServlet servlet;
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);

    @BeforeEach
    void setUp() {
        final var mapping = new AnnotationHandlerMapping("com.techcourse.controller");
        mapping.initialize();
        servlet = new TestDispatcherServlet(mapping);
    }

    @Test
    void annotationControllerShowsRegistrationForm() throws Exception {
        when(request.getRequestURI()).thenReturn("/register");
        when(request.getMethod()).thenReturn("GET");
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(dispatcher);

        servlet.service(request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    void annotationControllerSavesUserAndRedirects() throws Exception {
        when(request.getRequestURI()).thenReturn("/register");
        when(request.getMethod()).thenReturn("POST");
        when(request.getParameter("account")).thenReturn("registered-user");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getParameter("email")).thenReturn("user@example.com");

        servlet.service(request, response);

        assertThat(InMemoryUserRepository.findByAccount("registered-user")).isPresent();
        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    void annotationControllerShowsRegistrationView() throws Exception {
        when(request.getRequestURI()).thenReturn("/register/view");
        when(request.getMethod()).thenReturn("GET");
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(dispatcher);

        servlet.service(request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    void homeShowsIndex() throws Exception {
        when(request.getRequestURI()).thenReturn("/");
        when(request.getMethod()).thenReturn("GET");
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(dispatcher);

        servlet.service(request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    void loginViewShowsForm() throws Exception {
        when(request.getRequestURI()).thenReturn("/login/view");
        when(request.getMethod()).thenReturn("GET");
        when(request.getSession()).thenReturn(mock(HttpSession.class));
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/login.jsp")).thenReturn(dispatcher);

        servlet.service(request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    void loginStoresUserInSessionAndRedirects() throws Exception {
        final var user = new User(4, "login-user", "password", "user@example.com");
        InMemoryUserRepository.save(user);
        when(request.getRequestURI()).thenReturn("/login");
        when(request.getMethod()).thenReturn("POST");
        when(request.getParameter("account")).thenReturn("login-user");
        when(request.getParameter("password")).thenReturn("password");
        final var session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);

        servlet.service(request, response);

        verify(session).setAttribute(UserSession.SESSION_KEY, user);
        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    void wrongPasswordRedirectsToUnauthorizedPage() throws Exception {
        InMemoryUserRepository.save(new User(5, "wrong-password-user", "password", "user@example.com"));
        when(request.getRequestURI()).thenReturn("/login");
        when(request.getMethod()).thenReturn("POST");
        when(request.getParameter("account")).thenReturn("wrong-password-user");
        when(request.getParameter("password")).thenReturn("wrong");
        when(request.getSession()).thenReturn(mock(HttpSession.class));

        servlet.service(request, response);

        verify(response).sendRedirect("/401.jsp");
    }

    @Test
    void logoutRemovesSessionUserAndRedirects() throws Exception {
        when(request.getRequestURI()).thenReturn("/logout");
        when(request.getMethod()).thenReturn("GET");
        final var session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);

        servlet.service(request, response);

        verify(session).removeAttribute(UserSession.SESSION_KEY);
        verify(response).sendRedirect("/");
    }

    @Test
    void returnsUserAsJson() throws Exception {
        InMemoryUserRepository.save(new User(3, "json-user", "password", "user@example.com"));
        when(request.getRequestURI()).thenReturn("/api/user");
        when(request.getMethod()).thenReturn("GET");
        when(request.getParameter("account")).thenReturn("json-user");
        final var body = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(body));

        servlet.service(request, response);

        verify(response).setContentType("application/json;charset=UTF-8");
        assertThat(body.toString()).isEqualTo("{\"account\":\"json-user\"}");
    }

    @Test
    void unmappedRequestReturnsNotFound() throws Exception {
        when(request.getRequestURI()).thenReturn("/missing");
        when(request.getMethod()).thenReturn("GET");

        servlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    private static class TestDispatcherServlet extends DispatcherServlet {

        TestDispatcherServlet(final AnnotationHandlerMapping mapping) {
            super(List.of(mapping), List.of(new HandlerExecutionAdapter()));
        }

        @Override
        public void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
            super.service(request, response);
        }
    }
}
