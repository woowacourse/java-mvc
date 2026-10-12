package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HandlerExecutionHandlerAdapterTest {

    private final HandlerExecutionHandlerAdapter handlerAdapter = new HandlerExecutionHandlerAdapter();

    @Test
    void HandlerExecution을_실행한다() throws Exception {
        final var handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMapping.initialize();

        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        final Object handler = handlerMapping.getHandler(request);

        assertThat(handlerAdapter.supports(handler)).isTrue();
        assertThat(handlerAdapter.handle(request, response, handler).getObject("id")).isEqualTo("gugu");
    }

    @Test
    void HandlerExecution이_아니면_지원하지_않는다() {
        assertThat(handlerAdapter.supports(new Object())).isFalse();
    }
}
