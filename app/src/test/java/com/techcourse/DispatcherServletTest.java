package com.techcourse;

import com.techcourse.repository.InMemoryUserRepository;
import com.techcourse.controller.UserSession;
import jakarta.servlet.RequestDispatcher;
import static org.assertj.core.api.Assertions.assertThat;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class DispatcherServletTest {

    private DispatcherServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        servlet = new DispatcherServlet();
        servlet.init();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        when(request.getMethod()).thenReturn("GET");
        when(request.getSession()).thenReturn(mock(HttpSession.class));
    }

    @Test
    void rendersLegacyController() throws Exception {
        when(request.getRequestURI()).thenReturn("/login/view");
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/login.jsp")).thenReturn(dispatcher);

        servlet.service(request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    void rendersAnnotatedControllerWithModel() throws Exception {
        when(request.getRequestURI()).thenReturn("/integration");
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/integration.jsp")).thenReturn(dispatcher);

        servlet.service(request, response);

        verify(request).setAttribute("message", "hello");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void prefersAnnotationMappingWhenBothMatch() throws Exception {
        when(request.getRequestURI()).thenReturn("/logout");

        servlet.service(request, response);

        verify(response).sendRedirect("/integration");
    }

    @Test
    void returnsNotFoundForUnmappedRequest() throws Exception {
        when(request.getRequestURI()).thenReturn("/missing");

        servlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void returnsNotFoundForUnmappedHttpMethod() throws Exception {
        when(request.getRequestURI()).thenReturn("/integration");
        when(request.getMethod()).thenReturn("POST");

        servlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
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
    void retainsLegacyRegistrationFormUrl() throws Exception {
        when(request.getRequestURI()).thenReturn("/register/view");
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(dispatcher);

        servlet.service(request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    void registersUserAndAllowsLegacyLogin() throws Exception {
        final var account = "registration-integration-user";
        when(request.getRequestURI()).thenReturn("/register");
        when(request.getMethod()).thenReturn("POST");
        when(request.getParameter("account")).thenReturn(account);
        when(request.getParameter("password")).thenReturn("secret");
        when(request.getParameter("email")).thenReturn("test@example.com");

        servlet.service(request, response);

        final var user = InMemoryUserRepository.findByAccount(account).orElseThrow();
        assertThat(user.checkPassword("secret")).isTrue();
        verify(response).sendRedirect("/index.jsp");

        final var loginResponse = mock(HttpServletResponse.class);
        when(request.getRequestURI()).thenReturn("/login");
        servlet.service(request, loginResponse);

        verify(request.getSession()).setAttribute(UserSession.SESSION_KEY, user);
        verify(loginResponse).sendRedirect("/index.jsp");
    }

    @Test
    void rejectsUnmappedRegistrationMethod() throws Exception {
        when(request.getRequestURI()).thenReturn("/register");
        when(request.getMethod()).thenReturn("DELETE");

        servlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
    }
}
