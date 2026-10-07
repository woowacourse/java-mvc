package com.interface21.webmvc.servlet;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HandlerMappingRegistryTest {

    private HandlerMappingRegistry handlerMappingRegistry;
    private HandlerMapping first;
    private HandlerMapping second;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        handlerMappingRegistry = new HandlerMappingRegistry();
        first = mock(HandlerMapping.class);
        second = mock(HandlerMapping.class);
        request = mock(HttpServletRequest.class);
        handlerMappingRegistry.addHandlerMapping(first);
        handlerMappingRegistry.addHandlerMapping(second);
    }

    @Test
    void 여러_매핑이_핸들러를_찾으면_먼저_등록된_매핑의_핸들러를_반환한다() {
        final Object firstHandler = new Object();
        when(first.getHandler(request)).thenReturn(firstHandler);
        when(second.getHandler(request)).thenReturn(new Object());

        assertThat(handlerMappingRegistry.getHandler(request)).containsSame(firstHandler);
    }

    @Test
    void 앞의_매핑이_핸들러를_찾지_못하면_다음_매핑의_핸들러를_반환한다() {
        final Object secondHandler = new Object();
        when(second.getHandler(request)).thenReturn(secondHandler);

        assertThat(handlerMappingRegistry.getHandler(request)).containsSame(secondHandler);
    }

    @Test
    void 모든_매핑이_핸들러를_찾지_못하면_빈_Optional을_반환한다() {
        assertThat(handlerMappingRegistry.getHandler(request)).isEmpty();
    }
}
