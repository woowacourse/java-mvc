package com.interface21.webmvc.servlet.mvc;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.ModelAndView;
import com.interface21.webmvc.adapter.RequestHandlerAdapter;
import com.interface21.webmvc.mapping.HandlerMapping;
import com.interface21.webmvc.handler.RequestHandler;
import com.interface21.webmvc.view.JspView;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RequestHandlerAdapterTest {

    private RequestHandlerAdapter requestHandlerAdapter;
    private HandlerMapping handlerMapping1;
    private HandlerMapping handlerMapping2;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private RequestHandler requestHandler;
    private ModelAndView modelAndView;

    @BeforeEach
    void setUp() {
        handlerMapping1 = mock(HandlerMapping.class);
        handlerMapping2 = mock(HandlerMapping.class);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        requestHandler = mock(RequestHandler.class);
        modelAndView = new ModelAndView(new JspView(""));

        requestHandlerAdapter = new RequestHandlerAdapter(List.of(handlerMapping1, handlerMapping2));
    }

    @Test
    void testHandleWithNoHandlerFound() {
        // given
        when(request.getRequestURI()).thenReturn("/invalid");
        when(request.getMethod()).thenReturn("GET");

        // when
        when(handlerMapping1.getHandler("GET", "/invalid")).thenReturn(null);
        when(handlerMapping2.getHandler("GET", "/invalid")).thenReturn(null);

        // then
        assertThrows(ServletException.class, () -> {
            requestHandlerAdapter.handle(request, response);
        });
    }
}
