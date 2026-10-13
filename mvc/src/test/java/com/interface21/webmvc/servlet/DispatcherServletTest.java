package com.interface21.webmvc.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DispatcherServletTest {

    @Test
    @DisplayName("전달받은 패키지에서 컨트롤러를 찾아 요청을 처리한다")
    void dispatchesControllerFromConfiguredPackage() throws Exception {
        final var dispatcherServlet = new DispatcherServlet("samples");
        dispatcherServlet.init();

        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");
        when(request.getAttribute("id")).thenReturn("123");
        when(request.getRequestDispatcher("")).thenReturn(requestDispatcher);

        dispatcherServlet.service(request, response);

        verify(request).setAttribute("id", "123");
        verify(requestDispatcher).forward(request, response);
    }
}
