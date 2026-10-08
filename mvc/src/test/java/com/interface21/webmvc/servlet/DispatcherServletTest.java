package com.interface21.webmvc.servlet;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DispatcherServletTest {

    private DispatcherServlet dispatcherServlet;
    private HandlerMapping handlerMapping;
    private HandlerAdapter handlerAdapter;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        dispatcherServlet = new DispatcherServlet();
        handlerMapping = mock(HandlerMapping.class);
        handlerAdapter = mock(HandlerAdapter.class);
        dispatcherServlet.addHandlerMapping(handlerMapping);
        dispatcherServlet.addHandlerAdapter(handlerAdapter);
        dispatcherServlet.init();

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    void service_WhenHandlerFound_ThenRenderViewWithModel() throws Exception {
        final Object handler = new Object();
        final View view = mock(View.class);
        final ModelAndView modelAndView = new ModelAndView(view).addObject("id", "gugu");
        when(handlerMapping.getHandler(request)).thenReturn(handler);
        when(handlerAdapter.supports(handler)).thenReturn(true);
        when(handlerAdapter.handle(request, response, handler)).thenReturn(modelAndView);

        dispatcherServlet.service(request, response);

        verify(view).render(modelAndView.getModel(), request, response);
    }

    @Test
    void service_WhenNoHandler_ThenSendNotFound() throws Exception {
        when(request.getRequestURI()).thenReturn("/none");
        when(request.getMethod()).thenReturn("GET");

        dispatcherServlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
    }
}
