package com.interface21.webmvc.servlet;

import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecutionAdapter;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class DispatcherServletTest {

    private DispatcherServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        final var handlerMapping = new AnnotationHandlerMapping("dispatchersamples");
        handlerMapping.initialize();
        servlet = new DispatcherServlet(List.of(handlerMapping), List.of(new HandlerExecutionAdapter()));
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        when(request.getMethod()).thenReturn("GET");
    }

    @Test
    void rendersAnnotatedControllerWithModel() throws Exception {
        when(request.getRequestURI()).thenReturn("/integration");
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/integration.jsp")).thenReturn(dispatcher);

        servlet.service(request, response);

        verify(request).setAttribute("message", "hello");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void returnsNotFoundForUnmappedRequest() throws Exception {
        when(request.getRequestURI()).thenReturn("/missing");

        servlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void returnsNotFoundForUnmappedHttpMethod() throws Exception {
        when(request.getRequestURI()).thenReturn("/integration");
        when(request.getMethod()).thenReturn("POST");

        servlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void dispatchesUsingInjectedMappingsAndAdapters() throws Exception {
        final var firstMapping = mock(HandlerMapping.class);
        final var secondMapping = mock(HandlerMapping.class);
        final var unsupportedAdapter = mock(HandlerAdapter.class);
        final var supportedAdapter = mock(HandlerAdapter.class);
        final var handler = new Object();
        final var view = mock(View.class);
        final var modelAndView = new ModelAndView(view).addObject("message", "hello");
        when(secondMapping.getHandler(request)).thenReturn(handler);
        when(supportedAdapter.supports(handler)).thenReturn(true);
        when(supportedAdapter.handle(request, response, handler)).thenReturn(modelAndView);
        servlet = new DispatcherServlet(List.of(firstMapping, secondMapping),
                List.of(unsupportedAdapter, supportedAdapter));

        servlet.service(request, response);

        final var order = inOrder(firstMapping, secondMapping, unsupportedAdapter, supportedAdapter, view);
        order.verify(firstMapping).getHandler(request);
        order.verify(secondMapping).getHandler(request);
        order.verify(unsupportedAdapter).supports(handler);
        order.verify(supportedAdapter).supports(handler);
        order.verify(supportedAdapter).handle(request, response, handler);
        order.verify(view).render(modelAndView.getModel(), request, response);
        verify(unsupportedAdapter, never()).handle(any(), any(), any());
    }

}
