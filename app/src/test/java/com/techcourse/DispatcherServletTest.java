package com.techcourse;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMapping;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DispatcherServletTest {

    @Test
    void serviceIfRedirect() throws Exception {
        //given
        HandlerMappings handlerMappings = mock(HandlerMappings.class);
        HandlerExecutor handlerExecutor = mock(HandlerExecutor.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DispatcherServlet dispatcherServlet = new DispatcherServlet(handlerMappings, handlerExecutor);

        when(request.getMethod()).thenReturn("POST");
        when(handlerExecutor.execute(any(), any(), any())).thenReturn("redirect:/index.jsp");

        //when
        dispatcherServlet.service(request, response);

        //then
        verify(handlerExecutor, times(1)).execute(any(), any(), any());
        verify(response, times(1)).sendRedirect(any());
    }

    @Test
    void service() throws Exception {
        //given
        HandlerMappings handlerMappings = mock(HandlerMappings.class);
        HandlerExecutor handlerExecutor = mock(HandlerExecutor.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

        DispatcherServlet dispatcherServlet = new DispatcherServlet(handlerMappings, handlerExecutor);

        when(request.getMethod()).thenReturn("GET");
        when(handlerExecutor.execute(any(), any(), any())).thenReturn("/index.jsp");
        when(request.getRequestDispatcher(any())).thenReturn(requestDispatcher);

        //when
        dispatcherServlet.service(request, response);

        //then
        verify(handlerExecutor, times(1)).execute(any(), any(), any());
        verify(requestDispatcher, times(1)).forward(any(), any());
    }
}
