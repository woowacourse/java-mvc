package com.interface21.webmvc.servlet;

import com.interface21.web.http.MediaType;
import com.techcourse.controller.UserSession;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DispatcherServletIntegrationTest {

    @Test
    void storesUserSessionAfterSuccessfulLogin() throws Exception {
        final var request = loginRequest("gugu", "password");
        final var response = mock(HttpServletResponse.class);
        final var user = InMemoryUserRepository.findByAccount("gugu").orElseThrow();
        final var servlet = new DispatcherServlet("com.techcourse.controller");
        servlet.init();

        servlet.service(request, response);

        verify(request.getSession()).setAttribute(UserSession.SESSION_KEY, user);
        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    void rejectsLoginWithIncorrectPassword() throws Exception {
        final var request = loginRequest("gugu", "wrong-password");
        final var response = mock(HttpServletResponse.class);
        final var servlet = new DispatcherServlet("com.techcourse.controller");
        servlet.init();

        servlet.service(request, response);

        verify(request.getSession(), never()).setAttribute(anyString(), any());
        verify(response).sendRedirect("/401.jsp");
    }

    @Test
    void rejectsLoginWithUnknownAccount() throws Exception {
        final var request = loginRequest(UUID.randomUUID().toString(), "password");
        final var response = mock(HttpServletResponse.class);
        final var servlet = new DispatcherServlet("com.techcourse.controller");
        servlet.init();

        servlet.service(request, response);

        verify(request.getSession(), never()).setAttribute(anyString(), any());
        verify(response).sendRedirect("/401.jsp");
    }

    @Test
    void preservesExistingSessionWithoutReadingLoginCredentials() throws Exception {
        final var request = loginRequest("gugu", "wrong-password");
        final var response = mock(HttpServletResponse.class);
        final var user = InMemoryUserRepository.findByAccount("gugu").orElseThrow();
        when(request.getSession().getAttribute(UserSession.SESSION_KEY)).thenReturn(user);
        final var servlet = new DispatcherServlet("com.techcourse.controller");
        servlet.init();

        servlet.service(request, response);

        verify(request, never()).getParameter(anyString());
        verify(request.getSession(), never()).setAttribute(anyString(), any());
        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    void preservesLoginGetRequest() throws Exception {
        final var request = loginRequest("gugu", "password");
        when(request.getMethod()).thenReturn("GET");
        final var response = mock(HttpServletResponse.class);
        final var servlet = new DispatcherServlet("com.techcourse.controller");
        servlet.init();

        servlet.service(request, response);

        verify(request.getSession()).setAttribute(UserSession.SESSION_KEY,
                InMemoryUserRepository.findByAccount("gugu").orElseThrow());
        verify(response).sendRedirect("/index.jsp");
    }

    private HttpServletRequest loginRequest(final String account, final String password) {
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/login");
        when(request.getMethod()).thenReturn("POST");
        when(request.getSession()).thenReturn(mock(HttpSession.class));
        when(request.getParameter("account")).thenReturn(account);
        when(request.getParameter("password")).thenReturn(password);
        return request;
    }

    @Test
    void returnsUserAsJson() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var body = new StringWriter();
        when(request.getRequestURI()).thenReturn("/api/user");
        when(request.getMethod()).thenReturn("GET");
        when(request.getParameter("account")).thenReturn("gugu");
        when(response.getWriter()).thenReturn(new PrintWriter(body));
        final var servlet = new DispatcherServlet("com.techcourse.controller");
        servlet.init();

        servlet.service(request, response);

        assertEquals("{\"account\":\"gugu\"}", body.toString());
        final var order = inOrder(response);
        order.verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        order.verify(response).getWriter();
        verify(response, never()).sendError(HttpServletResponse.SC_NOT_FOUND);
        verify(response, never()).sendRedirect(anyString());
        verify(request, never()).getRequestDispatcher(anyString());
    }

    @Test
    void returnsNotFoundForUnsupportedUserApiMethod() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(request.getRequestURI()).thenReturn("/api/user");
        when(request.getMethod()).thenReturn("POST");
        final var servlet = new DispatcherServlet("com.techcourse.controller");
        servlet.init();

        servlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
        verify(response, never()).getWriter();
    }

    @Test
    void forwardsRootRequestToIndex() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestURI()).thenReturn("/");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(requestDispatcher);
        final var servlet = new DispatcherServlet("com.techcourse.controller");
        servlet.init();

        servlet.service(request, response);

        verify(requestDispatcher).forward(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    void removesUserSessionAndRedirectsLogoutRequestToRoot() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var session = mock(HttpSession.class);
        when(request.getRequestURI()).thenReturn("/logout");
        when(request.getMethod()).thenReturn("GET");
        when(request.getSession()).thenReturn(session);
        final var servlet = new DispatcherServlet("com.techcourse.controller");
        servlet.init();

        servlet.service(request, response);

        verify(session).removeAttribute(UserSession.SESSION_KEY);
        verify(response).sendRedirect("/");
        verify(request, never()).getRequestDispatcher(anyString());
    }

    @Test
    void handlesRootAndModelRequestsWithTheSameServlet() throws Exception {
        final var rootRequest = mock(HttpServletRequest.class);
        final var rootResponse = mock(HttpServletResponse.class);
        final var indexDispatcher = mock(RequestDispatcher.class);
        when(rootRequest.getRequestURI()).thenReturn("/");
        when(rootRequest.getMethod()).thenReturn("GET");
        when(rootRequest.getRequestDispatcher("/index.jsp")).thenReturn(indexDispatcher);

        final var annotatedRequest = mock(HttpServletRequest.class);
        final var annotatedResponse = mock(HttpServletResponse.class);
        final var profileDispatcher = mock(RequestDispatcher.class);
        when(annotatedRequest.getRequestURI()).thenReturn("/annotation-test");
        when(annotatedRequest.getMethod()).thenReturn("GET");
        when(annotatedRequest.getParameter("id")).thenReturn("gugu");
        when(annotatedRequest.getRequestDispatcher("/profile.jsp")).thenReturn(profileDispatcher);

        final var servlet = new DispatcherServlet("com.techcourse.controller");
        servlet.init();

        servlet.service(rootRequest, rootResponse);
        servlet.service(annotatedRequest, annotatedResponse);

        verify(indexDispatcher).forward(rootRequest, rootResponse);
        verify(annotatedRequest).setAttribute("id", "gugu");
        verify(profileDispatcher).forward(annotatedRequest, annotatedResponse);
        verify(annotatedResponse, never()).sendRedirect(anyString());
    }

    @Test
    void selectsAnnotatedPostHandlerAndRedirects() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(request.getRequestURI()).thenReturn("/annotation-test");
        when(request.getMethod()).thenReturn("POST");
        final var servlet = new DispatcherServlet("com.techcourse.controller");
        servlet.init();

        servlet.service(request, response);

        verify(response).sendRedirect("/annotation-test");
        verify(request, never()).getRequestDispatcher(anyString());
    }

    @Test
    void forwardsLoginViewForAnonymousUser() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestURI()).thenReturn("/login/view");
        when(request.getMethod()).thenReturn("GET");
        when(request.getSession()).thenReturn(mock(HttpSession.class));
        when(request.getRequestDispatcher("/login.jsp")).thenReturn(dispatcher);
        final var servlet = new DispatcherServlet("com.techcourse.controller");
        servlet.init();

        servlet.service(request, response);

        verify(dispatcher).forward(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    void redirectsLoginViewForLoggedInUser() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var session = mock(HttpSession.class);
        final var user = InMemoryUserRepository.findByAccount("gugu").orElseThrow();
        when(request.getRequestURI()).thenReturn("/login/view");
        when(request.getMethod()).thenReturn("GET");
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(UserSession.SESSION_KEY)).thenReturn(user);
        final var servlet = new DispatcherServlet("com.techcourse.controller");
        servlet.init();

        servlet.service(request, response);

        verify(response).sendRedirect("/index.jsp");
        verify(request, never()).getRequestDispatcher(anyString());
        verify(session, never()).setAttribute(anyString(), any());
        verify(session, never()).removeAttribute(anyString());
    }

    @Test
    void preservesLoginViewPostRequest() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestURI()).thenReturn("/login/view");
        when(request.getMethod()).thenReturn("POST");
        when(request.getSession()).thenReturn(mock(HttpSession.class));
        when(request.getRequestDispatcher("/login.jsp")).thenReturn(dispatcher);
        final var servlet = new DispatcherServlet("com.techcourse.controller");
        servlet.init();

        servlet.service(request, response);

        verify(dispatcher).forward(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    void returnsNotFoundForHttpMethodOutsideAnnotationSupport() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(request.getRequestURI()).thenReturn("/");
        when(request.getMethod()).thenReturn("PROPFIND");
        final var servlet = new DispatcherServlet("com.techcourse.controller");
        servlet.init();

        servlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
        verify(request, never()).getRequestDispatcher(anyString());
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    void returnsNotFoundWhenNoMappingMatches() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(request.getRequestURI()).thenReturn("/unregistered");
        when(request.getMethod()).thenReturn("GET");
        final var servlet = new DispatcherServlet("com.techcourse.controller");
        servlet.init();

        servlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
        verify(request, never()).getRequestDispatcher(anyString());
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    void forwardsRegistrationGetWithoutSavingUser() throws Exception {
        final var account = UUID.randomUUID().toString();
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestURI()).thenReturn("/register");
        when(request.getMethod()).thenReturn("GET");
        when(request.getParameter("account")).thenReturn(account);
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(dispatcher);
        final var servlet = new DispatcherServlet("com.techcourse.controller");
        servlet.init();

        servlet.service(request, response);

        assertFalse(InMemoryUserRepository.findByAccount(account).isPresent());
        verify(dispatcher).forward(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    void savesRegistrationPostAndRedirects() throws Exception {
        final var account = UUID.randomUUID().toString();
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(request.getRequestURI()).thenReturn("/register");
        when(request.getMethod()).thenReturn("POST");
        when(request.getParameter("account")).thenReturn(account);
        when(request.getParameter("password")).thenReturn("password");
        when(request.getParameter("email")).thenReturn("test@example.com");
        final var servlet = new DispatcherServlet("com.techcourse.controller");
        servlet.init();

        servlet.service(request, response);

        final var savedUser = InMemoryUserRepository.findByAccount(account).orElseThrow();
        assertEquals(account, savedUser.getAccount());
        assertTrue(savedUser.checkPassword("password"));
        verify(response).sendRedirect("/index.jsp");
        verify(request, never()).getRequestDispatcher(anyString());
    }

    @Test
    void preservesLegacyRegistrationViewLink() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestURI()).thenReturn("/register/view");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(dispatcher);
        final var servlet = new DispatcherServlet("com.techcourse.controller");
        servlet.init();

        servlet.service(request, response);

        verify(dispatcher).forward(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    void doesNotSaveRegistrationForUnmappedHttpMethod() throws Exception {
        final var account = UUID.randomUUID().toString();
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(request.getRequestURI()).thenReturn("/register");
        when(request.getMethod()).thenReturn("PUT");
        when(request.getParameter("account")).thenReturn(account);
        final var servlet = new DispatcherServlet("com.techcourse.controller");
        servlet.init();

        servlet.service(request, response);

        assertFalse(InMemoryUserRepository.findByAccount(account).isPresent());
        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
        verify(response, never()).sendRedirect(anyString());
        verify(request, never()).getRequestDispatcher(anyString());
    }
}
