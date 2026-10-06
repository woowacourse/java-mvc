package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.techcourse.controller.UserSession;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    }

    @Test
    void legacyControllerForwardsToJsp() throws Exception {
        givenRequest("GET", "/register/view");
        RequestDispatcher dispatcher = givenDispatcher("/register.jsp");

        servlet.service(request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    void legacyControllerRedirects() throws Exception {
        givenRequest("GET", "/logout");
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);

        servlet.service(request, response);

        verify(session).removeAttribute(UserSession.SESSION_KEY);
        verify(response).sendRedirect("/");
    }

    @Test
    void annotatedGetControllerForwardsToJsp() throws Exception {
        givenRequest("GET", "/register");
        RequestDispatcher dispatcher = givenDispatcher("/register.jsp");

        servlet.service(request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    void annotatedPostControllerSavesUserAndRedirects() throws Exception {
        givenRequest("POST", "/register");
        String account = "step2-" + UUID.randomUUID();
        when(request.getParameter("account")).thenReturn(account);
        when(request.getParameter("password")).thenReturn("password");
        when(request.getParameter("email")).thenReturn("test@example.com");

        servlet.service(request, response);

        assertThat(InMemoryUserRepository.findByAccount(account)).isPresent();
        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    void unsupportedHttpMethodForAnnotatedPathReturnsNotFound() throws Exception {
        givenRequest("DELETE", "/register");

        servlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    private void givenRequest(String method, String requestURI) {
        when(request.getMethod()).thenReturn(method);
        when(request.getRequestURI()).thenReturn(requestURI);
    }

    private RequestDispatcher givenDispatcher(String path) {
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher(path)).thenReturn(dispatcher);
        return dispatcher;
    }
}
