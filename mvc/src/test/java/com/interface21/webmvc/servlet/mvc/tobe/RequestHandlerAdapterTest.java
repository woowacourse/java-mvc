package com.interface21.webmvc.servlet.mvc.tobe;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.HandlerMapping;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.RequestHandler;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

    @DisplayName("ModelAndView를 반환")
    @Test
    void testHandleWithValidHandler() throws Exception {
        // given
        when(request.getRequestURI()).thenReturn("/valid");
        when(request.getMethod()).thenReturn("GET");

        // when
        when(handlerMapping1.getHandler("GET", "/valid")).thenReturn(null);
        when(handlerMapping2.getHandler("GET", "/valid")).thenReturn(requestHandler);
        when(requestHandler.handle(request, response)).thenReturn(modelAndView);

        // then
        ModelAndView result = requestHandlerAdapter.handle(request, response);

        assertNotNull(result);
        verify(requestHandler).handle(request, response);
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

    @Test
    void testHandleWithFirstHandlerMapping() throws Exception {
        // given
        when(request.getRequestURI()).thenReturn("/first");
        when(request.getMethod()).thenReturn("POST");

        // when
        when(handlerMapping1.getHandler("POST", "/first")).thenReturn(requestHandler);
        when(requestHandler.handle(request, response)).thenReturn(modelAndView);

        // then
        ModelAndView result = requestHandlerAdapter.handle(request, response);

        assertNotNull(result);
        verify(handlerMapping2, never()).getHandler(anyString(),
                anyString());  // second handlerMapping should not be invoked
        verify(requestHandler).handle(request, response);
    }
}
