package com.techcourse;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class DispatcherServletTest {

    private DispatcherServlet dispatcherServlet;

    @BeforeEach
    public void setUp() {
        dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();
    }

    @DisplayName("어노테이션 기반의 컨트롤러 요청을 처리한다")
    @Test
    public void processAnnotationController() throws Exception {
        // Given
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        RequestDispatcher requestDispatcher = Mockito.mock(RequestDispatcher.class);

        when(request.getRequestURI()).thenReturn("/register");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(requestDispatcher);

        // When
        dispatcherServlet.service(request, response);

        // Then
        verify(requestDispatcher).forward(request, response);
    }

    @DisplayName("레거시 컨트롤러 요청을 처리한다")
    @Test
    public void processLegacyController() throws Exception {
        // Given
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        RequestDispatcher requestDispatcher = Mockito.mock(RequestDispatcher.class);

        when(request.getRequestURI()).thenReturn("/");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(requestDispatcher);

        // When
        dispatcherServlet.service(request, response);

        // Then
        verify(requestDispatcher).forward(request, response);
    }
}
