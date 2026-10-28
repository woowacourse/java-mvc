package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

class HandlerExecutionHandlerAdapterTest {

    private final HandlerExecutionHandlerAdapter handlerAdapter = new HandlerExecutionHandlerAdapter();

    @Test
    void supportsHandlerExecution() throws Exception {
        final var handlerExecution = findHandlerExecution("/get-test", "GET");

        assertThat(handlerAdapter.supports(handlerExecution)).isTrue();
        assertThat(handlerAdapter.supports(new Object())).isFalse();
    }

    @Test
    void handleHandlerExecution() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(request.getAttribute("id")).thenReturn("gugu");
        final var handlerExecution = findHandlerExecution("/get-test", "GET");

        final var modelAndView = handlerAdapter.handle(request, response, handlerExecution);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    private Object findHandlerExecution(final String uri, final String method) {
        final var handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMapping.initialize();
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn(uri);
        when(request.getMethod()).thenReturn(method);
        return handlerMapping.getHandler(request);
    }
}
