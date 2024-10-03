package com.interface21.webmvc.servlet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class DispatcherServletTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private ServletContext servletContext;
    private ArgumentCaptor<String> argumentCaptor;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        servletContext = mock(ServletContext.class);
        argumentCaptor = ArgumentCaptor.forClass(String.class);

        when(request.getSession()).thenReturn(mock(HttpSession.class));
        when(request.getRequestDispatcher(argumentCaptor.capture())).thenReturn(mock(RequestDispatcher.class));
        when(request.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute("basePackage")).thenReturn("samples");
    }

    @Test
    @Disabled("Controller 핸들러 지원 종료")
    @DisplayName("Controller 핸들러의 요청 처리 성공: login.jsp로 forward")
    void service_LoginRequest() throws ServletException {
        final DispatcherServlet dispatcherServlet = new DispatcherServlet(servletContext);
        dispatcherServlet.init();

        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/login");

        dispatcherServlet.service(request, response);

        assertThat(argumentCaptor.getValue()).isEqualTo("/login.jsp");
    }

    @Test
    @DisplayName("Annotation 핸들러의 요청 처리 성공: login.jsp로 forward")
    void service_RegisterRequest() throws ServletException {
        final DispatcherServlet dispatcherServlet = new DispatcherServlet(servletContext);
        dispatcherServlet.init();

        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/get-test");

        dispatcherServlet.service(request, response);

        assertThat(argumentCaptor.getValue()).isEqualTo("/fake.jsp");
    }
}
