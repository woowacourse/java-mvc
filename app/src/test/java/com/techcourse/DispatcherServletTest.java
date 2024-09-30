package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoSession;
import org.mockito.internal.matchers.CapturingMatcher;

class DispatcherServletTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private ArgumentCaptor<String> argumentCaptor;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        argumentCaptor = ArgumentCaptor.forClass(String.class);

        when(request.getSession()).thenReturn(mock(HttpSession.class));
        when(request.getRequestDispatcher(argumentCaptor.capture())).thenReturn(mock(RequestDispatcher.class));
    }

    @Test
    @DisplayName("Controller 핸들러의 요청 처리 성공: login.jsp로 forward")
    void service_LoginRequest() throws ServletException {
        final DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();

        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/login/view");

        dispatcherServlet.service(request, response);

        assertThat(argumentCaptor.getValue()).isEqualTo("/login.jsp");
    }

    @Test
    @DisplayName("Annotation 핸들러의 요청 처리 성공: login.jsp로 forward")
    void service_RegisterRequest() throws ServletException {
        final DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();

        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/register");

        dispatcherServlet.service(request, response);

        assertThat(argumentCaptor.getValue()).isEqualTo("/register.jsp");
    }
}
