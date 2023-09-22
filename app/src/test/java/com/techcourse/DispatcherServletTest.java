package com.techcourse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.junit.jupiter.api.Test;

class DispatcherServletTest {

    private HttpServletRequest request = mock(HttpServletRequest.class);
    private HttpServletResponse response = mock(HttpServletResponse.class);
    private RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

    @Test
    void 매뉴얼_작동_테스트() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestDispatcher(any())).thenReturn(requestDispatcher);

        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();
        dispatcherServlet.service(request, response);

        verify(requestDispatcher, times(1)).forward(any(), any());
    }

    @Test
    void 어노테이션_작동_테스트() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/register");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestDispatcher(any())).thenReturn(requestDispatcher);

        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();
        dispatcherServlet.service(request, response);

        verify(requestDispatcher, times(1)).forward(any(), any());
    }
}
