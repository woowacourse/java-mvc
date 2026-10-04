package com.interface21.webmvc.servlet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HandlerAdapterRegistryTest {

    private HandlerAdapterRegistry handlerAdapterRegistry;
    private HandlerAdapter first;
    private HandlerAdapter second;
    private Object handler;

    @BeforeEach
    void setUp() {
        handlerAdapterRegistry = new HandlerAdapterRegistry();
        first = mock(HandlerAdapter.class);
        second = mock(HandlerAdapter.class);
        handler = new Object();
        handlerAdapterRegistry.addHandlerAdapter(first);
        handlerAdapterRegistry.addHandlerAdapter(second);
    }

    @Test
    void 핸들러를_지원하는_어댑터를_반환한다() {
        when(second.supports(handler)).thenReturn(true);

        assertThat(handlerAdapterRegistry.getHandlerAdapter(handler)).isSameAs(second);
    }

    @Test
    void 핸들러를_지원하는_어댑터가_없으면_예외가_발생한다() {
        assertThatThrownBy(() -> handlerAdapterRegistry.getHandlerAdapter(handler))
                .isInstanceOf(IllegalStateException.class);
    }
}
