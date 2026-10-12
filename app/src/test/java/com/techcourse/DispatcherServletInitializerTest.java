package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.webmvc.servlet.DispatcherServlet;
import com.techcourse.controller.UserSession;
import com.techcourse.domain.User;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class DispatcherServletInitializerTest {

    @Test
    void givenServletContext_whenStarts_thenRegistersDispatcherServlet() {
        final var servletContext = mock(ServletContext.class);
        final var registration = mock(ServletRegistration.Dynamic.class);

        when(servletContext.addServlet(
                eq("dispatcher"),
                any(DispatcherServlet.class)
        )).thenReturn(registration);

        new DispatcherServletInitializer()
                .onStartup(servletContext);

        verify(servletContext)
                .addServlet(
                        eq("dispatcher"),
                        any(DispatcherServlet.class)
                );

        verify(registration).setLoadOnStartup(1);
        verify(registration).addMapping("/");
    }

    @Test
    void givenRootRequest_whenServices_thenRendersIndexView() throws Exception {
        final var dispatcherServlet = registerDispatcherServlet();
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var requestDispatcher = mock(RequestDispatcher.class);

        when(request.getRequestURI()).thenReturn("/");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(requestDispatcher);

        dispatcherServlet.service((ServletRequest) request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void givenRegisterViewRequest_whenServices_thenRendersRegisterView() throws Exception {
        final var dispatcherServlet = registerDispatcherServlet();
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var requestDispatcher = mock(RequestDispatcher.class);

        when(request.getRequestURI()).thenReturn("/register/view");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(requestDispatcher);

        dispatcherServlet.service((ServletRequest) request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void givenUserRequest_whenServices_thenRendersJsonResponse() throws Exception {
        final var dispatcherServlet = registerDispatcherServlet();
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var responseBody = new StringWriter();

        when(request.getRequestURI()).thenReturn("/api/user");
        when(request.getMethod()).thenReturn("GET");
        when(request.getParameter("account")).thenReturn("gugu");
        when(response.getWriter()).thenReturn(new PrintWriter(responseBody));

        dispatcherServlet.service((ServletRequest) request, response);

        verify(response).setContentType("application/json;charset=UTF-8");
        final var json = new ObjectMapper().readTree(responseBody.toString());
        assertThat(json.get("account").asText()).isEqualTo("gugu");
    }

    @Test
    void givenValidLoginRequest_whenServices_thenStoresUserAndRedirects() throws Exception {
        final var dispatcherServlet = registerDispatcherServlet();
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var session = mock(HttpSession.class);

        when(request.getRequestURI()).thenReturn("/login");
        when(request.getMethod()).thenReturn("POST");
        when(request.getParameter("account")).thenReturn("gugu");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getSession()).thenReturn(session);

        dispatcherServlet.service((ServletRequest) request, response);

        final var userCaptor = ArgumentCaptor.forClass(User.class);
        verify(session).setAttribute(eq(UserSession.SESSION_KEY), userCaptor.capture());
        assertThat(userCaptor.getValue().getAccount()).isEqualTo("gugu");
        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    void givenInvalidPassword_whenServicesLoginRequest_thenRedirectsToUnauthorizedView() throws Exception {
        final var dispatcherServlet = registerDispatcherServlet();
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var session = mock(HttpSession.class);

        when(request.getRequestURI()).thenReturn("/login");
        when(request.getMethod()).thenReturn("POST");
        when(request.getParameter("account")).thenReturn("gugu");
        when(request.getParameter("password")).thenReturn("wrong-password");
        when(request.getSession()).thenReturn(session);

        dispatcherServlet.service((ServletRequest) request, response);

        verify(response).sendRedirect("/401.jsp");
        verify(session, never()).setAttribute(eq(UserSession.SESSION_KEY), any());
    }

    @Test
    void givenLoginViewRequest_whenServices_thenRendersLoginView() throws Exception {
        final var dispatcherServlet = registerDispatcherServlet();
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var session = mock(HttpSession.class);
        final var requestDispatcher = mock(RequestDispatcher.class);

        when(request.getRequestURI()).thenReturn("/login/view");
        when(request.getMethod()).thenReturn("GET");
        when(request.getSession()).thenReturn(session);
        when(request.getRequestDispatcher("/login.jsp")).thenReturn(requestDispatcher);

        dispatcherServlet.service((ServletRequest) request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void givenAuthenticatedLoginViewRequest_whenServices_thenRedirectsToIndex() throws Exception {
        final var dispatcherServlet = registerDispatcherServlet();
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var session = mock(HttpSession.class);
        final var user = mock(User.class);

        when(request.getRequestURI()).thenReturn("/login/view");
        when(request.getMethod()).thenReturn("GET");
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(UserSession.SESSION_KEY)).thenReturn(user);

        dispatcherServlet.service((ServletRequest) request, response);

        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    void givenLogoutRequest_whenServices_thenRemovesUserAndRedirects() throws Exception {
        final var dispatcherServlet = registerDispatcherServlet();
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var session = mock(HttpSession.class);

        when(request.getRequestURI()).thenReturn("/logout");
        when(request.getMethod()).thenReturn("GET");
        when(request.getSession()).thenReturn(session);

        dispatcherServlet.service((ServletRequest) request, response);

        verify(session).removeAttribute(UserSession.SESSION_KEY);
        verify(response).sendRedirect("/");
    }

    private DispatcherServlet registerDispatcherServlet() {
        final var servletContext = mock(ServletContext.class);
        final var registration = mock(ServletRegistration.Dynamic.class);
        when(servletContext.addServlet(eq("dispatcher"), any(DispatcherServlet.class)))
                .thenReturn(registration);

        new DispatcherServletInitializer().onStartup(servletContext);

        final var dispatcherServletCaptor = ArgumentCaptor.forClass(DispatcherServlet.class);
        verify(servletContext).addServlet(eq("dispatcher"), dispatcherServletCaptor.capture());
        return dispatcherServletCaptor.getValue();
    }
}
