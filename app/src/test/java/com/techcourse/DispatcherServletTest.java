package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.webmvc.servlet.mvc.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import com.techcourse.controller.UserSession;
import com.techcourse.domain.User;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class DispatcherServletTest {

    private DispatcherServlet registerDispatcherServlet() throws ServletException {
        final var servletContext = mock(ServletContext.class);
        final var registration = mock(ServletRegistration.Dynamic.class);
        when(servletContext.addServlet(eq("dispatcher"), any(DispatcherServlet.class)))
                .thenReturn(registration);

        new DispatcherServletInitializer().onStartup(servletContext);

        final var dispatcherServletCaptor = ArgumentCaptor.forClass(DispatcherServlet.class);
        verify(servletContext).addServlet(eq("dispatcher"), dispatcherServletCaptor.capture());
        verify(registration).setLoadOnStartup(1);
        verify(registration).addMapping("/");

        final var dispatcherServlet = dispatcherServletCaptor.getValue();
        dispatcherServlet.init(mock(ServletConfig.class));
        return dispatcherServlet;
    }

    @Test
    void givenRootRequest_whenServices_thenRendersIndexView() throws Exception {
        final var dispatcherServlet = registerDispatcherServlet();
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var requestDispatcher = mock(RequestDispatcher.class);

        when(request.getRequestURI()).thenReturn("/");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestDispatcher("/index.jsp"))
                .thenReturn(requestDispatcher);

        dispatcherServlet.service(request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void givenAnnotationControllerRequest_whenServices_thenRendersView() throws Exception {
        final var dispatcherServlet = registerDispatcherServlet();
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var requestDispatcher = mock(RequestDispatcher.class);

        when(request.getRequestURI()).thenReturn("/register/view");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestDispatcher("/register.jsp"))
                .thenReturn(requestDispatcher);

        dispatcherServlet.service(request, response);

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

        dispatcherServlet.service(request, response);

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

        dispatcherServlet.service(request, response);

        final var userCaptor = ArgumentCaptor.forClass(User.class);

        verify(session).setAttribute(
                eq(UserSession.SESSION_KEY),
                userCaptor.capture()
        );

        assertThat(userCaptor.getValue().getAccount())
                .isEqualTo("gugu");

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
        when(request.getParameter("account"))
                .thenReturn("gugu");
        when(request.getParameter("password"))
                .thenReturn("wrong-password");
        when(request.getSession()).thenReturn(session);

        dispatcherServlet.service(request, response);

        verify(response).sendRedirect("/401.jsp");
        verify(session, never())
                .setAttribute(eq(UserSession.SESSION_KEY), any());
    }

    @Test
    void givenUnauthenticatedLoginViewRequest_whenServices_thenRendersLoginView() throws Exception {
        final var dispatcherServlet = registerDispatcherServlet();
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var session = mock(HttpSession.class);
        final var requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestURI()).thenReturn("/login/view");
        when(request.getMethod()).thenReturn("GET");
        when(request.getSession()).thenReturn(session);
        when(request.getRequestDispatcher("/login.jsp")).thenReturn(requestDispatcher);

        dispatcherServlet.service(request, response);

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

        dispatcherServlet.service(request, response);

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

        dispatcherServlet.service(request, response);

        verify(session).removeAttribute(UserSession.SESSION_KEY);
        verify(response).sendRedirect("/");
    }

    @Test
    void givenNoMatchingHandler_whenServices_thenSendsNotFoundAndStops() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var handlerMapping = (HandlerMapping) ignored -> null;
        final var handlerAdapter = mock(HandlerAdapter.class);
        final var dispatcherServlet = new DispatcherServlet(List.of(handlerMapping), List.of(handlerAdapter));

        dispatcherServlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
        verifyNoInteractions(handlerAdapter);
    }

    @Test
    void givenHandlerWithoutSupportingAdapter_whenServices_thenReportsHandlerTypeAndStops() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var handler = new Object();
        final var handlerMapping = (HandlerMapping) ignored -> handler;
        final var handlerAdapter = mock(HandlerAdapter.class);
        final var dispatcherServlet = new DispatcherServlet(List.of(handlerMapping), List.of(handlerAdapter));

        when(handlerAdapter.supports(handler)).thenReturn(false);

        assertThatThrownBy(() -> dispatcherServlet.service(request, response))
                .isInstanceOf(ServletException.class)
                .satisfies(exception -> {
                    assertThat(exception.getCause()).isInstanceOf(IllegalStateException.class);
                    assertThat(exception.getCause().getMessage()).contains(Object.class.getName());
                });

        verify(handlerAdapter, never()).handle(request, response, handler);
    }

    @Test
    void givenHandlerExecutionFails_whenServices_thenPreservesCause() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var handler = new Object();
        final var handlerMapping = (HandlerMapping) ignored -> handler;
        final var handlerAdapter = mock(HandlerAdapter.class);
        final var dispatcherServlet = new DispatcherServlet(List.of(handlerMapping), List.of(handlerAdapter));
        final var failure = new IllegalArgumentException("handler failed");

        when(handlerAdapter.supports(handler)).thenReturn(true);
        when(handlerAdapter.handle(request, response, handler)).thenThrow(failure);

        assertThatThrownBy(() -> dispatcherServlet.service(request, response))
                .isInstanceOf(ServletException.class)
                .hasCause(failure);
    }
}
