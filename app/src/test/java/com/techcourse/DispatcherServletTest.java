package com.techcourse;

import com.techcourse.repository.InMemoryUserRepository;
import com.techcourse.controller.UserSession;
import jakarta.servlet.RequestDispatcher;
import java.io.PrintWriter;
import java.io.StringWriter;
import com.interface21.web.http.MediaType;
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
    void rendersLoginView() throws Exception {
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
    void registersUserAndAllowsLogin() throws Exception {
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

    @Test
    void rendersUserApiAsJsonWithoutPassword() throws Exception {
        when(request.getRequestURI()).thenReturn("/api/user");
        when(request.getParameter("account")).thenReturn("gugu");
        final var output = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(output));

        servlet.service(request, response);

        assertThat(output.toString()).isEqualTo("{\"account\":\"gugu\"}");
        assertThat(output.toString()).doesNotContain("password");
        final var order = inOrder(response);
        order.verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        order.verify(response).getWriter();
    }

    @Test
    void rendersIndex() throws Exception {
        when(request.getRequestURI()).thenReturn("/");
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(dispatcher);

        servlet.service(request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    void rejectsWrongPassword() throws Exception {
        when(request.getRequestURI()).thenReturn("/login");
        when(request.getMethod()).thenReturn("POST");
        when(request.getParameter("account")).thenReturn("gugu");
        when(request.getParameter("password")).thenReturn("wrong");

        servlet.service(request, response);

        verify(response).sendRedirect("/401.jsp");
        verify(request.getSession(), never()).setAttribute(anyString(), any());
    }

    @Test
    void rejectsUnknownAccount() throws Exception {
        when(request.getRequestURI()).thenReturn("/login");
        when(request.getMethod()).thenReturn("POST");
        when(request.getParameter("account")).thenReturn("unknown-account");

        servlet.service(request, response);

        verify(response).sendRedirect("/401.jsp");
        verify(request.getSession(), never()).setAttribute(anyString(), any());
    }

    @Test
    void redirectsLoggedInUserFromLoginView() throws Exception {
        when(request.getRequestURI()).thenReturn("/login/view");
        when(request.getSession().getAttribute(UserSession.SESSION_KEY))
                .thenReturn(InMemoryUserRepository.findByAccount("gugu").orElseThrow());

        servlet.service(request, response);

        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    void redirectsAlreadyLoggedInUserFromLogin() throws Exception {
        when(request.getRequestURI()).thenReturn("/login");
        when(request.getMethod()).thenReturn("POST");
        when(request.getSession().getAttribute(UserSession.SESSION_KEY))
                .thenReturn(InMemoryUserRepository.findByAccount("gugu").orElseThrow());

        servlet.service(request, response);

        verify(response).sendRedirect("/index.jsp");
        verify(request.getSession(), never()).setAttribute(anyString(), any());
    }

    @Test
    void logsOutAndRedirectsHome() throws Exception {
        when(request.getRequestURI()).thenReturn("/logout");

        servlet.service(request, response);

        verify(request.getSession()).removeAttribute(UserSession.SESSION_KEY);
        verify(response).sendRedirect("/");
    }
}
