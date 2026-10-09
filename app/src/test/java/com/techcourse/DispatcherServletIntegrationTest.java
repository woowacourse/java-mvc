package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.DispatcherServlet;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecutionAdapter;
import com.techcourse.controller.UserSession;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DispatcherServletIntegrationTest {

    private DispatcherServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        AnnotationHandlerMapping mapping =
                new AnnotationHandlerMapping("com.techcourse.controller");
        mapping.initialize();

        servlet = new DispatcherServlet(
                List.of(mapping),
                List.of(new HandlerExecutionAdapter())
        );

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    void registerViewForwardsToJsp() throws Exception {
        givenRequest("GET", "/register/view");
        RequestDispatcher dispatcher = givenDispatcher("/register.jsp");

        servlet.service(request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    void logoutRemovesUserFromSessionAndRedirects() throws Exception {
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
        String account = "step3-" + UUID.randomUUID();
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
