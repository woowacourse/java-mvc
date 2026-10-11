package com.techcourse;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.ControllerHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecutionHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DispatcherServletTest {

    @Test
    void dispatchesLegacyAndAnnotationControllersThroughTheSameServlet() throws Exception {
        final DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();

        final HttpServletRequest loginRequest = request("POST", "/login");
        final HttpSession session = mock(HttpSession.class);
        when(loginRequest.getSession()).thenReturn(session);
        when(loginRequest.getParameter("account")).thenReturn("missing-account");

        final HttpServletResponse loginResponse = mock(HttpServletResponse.class);
        dispatcherServlet.service(loginRequest, loginResponse);

        verify(loginResponse).sendRedirect("/401.jsp");

        final HttpServletRequest registerGetRequest = request("GET", "/register");
        final HttpServletResponse registerGetResponse = mock(HttpServletResponse.class);
        final RequestDispatcher registerView = mock(RequestDispatcher.class);
        when(registerGetRequest.getRequestDispatcher("/register.jsp")).thenReturn(registerView);

        dispatcherServlet.service(registerGetRequest, registerGetResponse);

        verify(registerView).forward(registerGetRequest, registerGetResponse);

        final HttpServletRequest registerPostRequest = request("POST", "/register");
        when(registerPostRequest.getParameter("account")).thenReturn("review-feedback-test");
        when(registerPostRequest.getParameter("password")).thenReturn("password");
        when(registerPostRequest.getParameter("email")).thenReturn("review@example.com");

        final HttpServletResponse registerPostResponse = mock(HttpServletResponse.class);
        dispatcherServlet.service(registerPostRequest, registerPostResponse);

        verify(registerPostResponse).sendRedirect("/index.jsp");
    }

    @Test
    void prefersAnnotationMappingWhenBothMappingsMatchTheSamePath() throws Exception {
        final AnnotationHandlerMapping annotationHandlerMapping =
                new AnnotationHandlerMapping("com.techcourse.controller");
        annotationHandlerMapping.initialize();

        final HandlerMapping legacyMapping = request -> {
            if ("/register".equals(request.getRequestURI())) {
                return (Controller) (httpRequest, httpResponse) -> "/legacy-register.jsp";
            }
            return null;
        };
        final DispatcherServlet dispatcherServlet = new DispatcherServlet(
                List.of(annotationHandlerMapping, legacyMapping),
                List.of(new HandlerExecutionHandlerAdapter(), new ControllerHandlerAdapter()));

        final HttpServletRequest request = request("GET", "/register");
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final RequestDispatcher annotationView = mock(RequestDispatcher.class);
        final RequestDispatcher legacyView = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(annotationView);
        when(request.getRequestDispatcher("/legacy-register.jsp")).thenReturn(legacyView);

        dispatcherServlet.service(request, response);

        verify(annotationView).forward(request, response);
        verify(legacyView, never()).forward(request, response);
    }

    private HttpServletRequest request(final String method, final String requestURI) {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn(method);
        when(request.getRequestURI()).thenReturn(requestURI);
        when(request.getContextPath()).thenReturn("");
        return request;
    }
}
