package com.interface21.webmvc.servlet.mvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
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
        ServletContext servletContext = mock(ServletContext.class);
        when(servletContext.getAttribute("basePackage"))
                .thenReturn("samples");
        dispatcherServlet = new DispatcherServlet(servletContext);
        dispatcherServlet.init();
    }

    @DisplayName("어노테이션 기반의 컨트롤러 요청을 처리한다")
    @Test
    public void processAnnotationController() throws Exception {
        // Given
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        RequestDispatcher requestDispatcher = Mockito.mock(RequestDispatcher.class);

        when(request.getRequestURI()).thenReturn("/get-hi-jsp");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestDispatcher(any())).thenReturn(requestDispatcher);

        // When
        dispatcherServlet.service(request, response);

        // Then
        verify(requestDispatcher).forward(request, response);
    }
}
