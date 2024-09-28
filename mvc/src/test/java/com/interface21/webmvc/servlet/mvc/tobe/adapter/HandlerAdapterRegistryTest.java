package com.interface21.webmvc.servlet.mvc.tobe.adapter;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerAdapterRegistryTest {

    private HandlerAdapterRegistry handlerAdapterRegistry;
    private HandlerAdapter handlerAdapter;
    private Object handler;

    @BeforeEach
    void setUp() {
        handlerAdapterRegistry = new HandlerAdapterRegistry();
        handlerAdapter = mock(HandlerAdapter.class);
        handlerAdapterRegistry.addHandlerAdapter(handlerAdapter);
    }

    @DisplayName("요청에 맞는 handlerAdapter를 반환한다.")
    @Test
    void getHandlerAdapter() {
        when(handlerAdapter.supports(handler)).thenReturn(true);

        assertThatCode(() -> handlerAdapterRegistry.getHandlerAdapter(handler))
                .doesNotThrowAnyException();
    }

    @DisplayName("요청에 맞는 handlerAdapter가 없으면 예외가 발생한다.")
    @Test
    void getHandlerAdapterThrowException() {
        when(handlerAdapter.supports(handler)).thenReturn(false);

        assertThatThrownBy(() -> handlerAdapterRegistry.getHandlerAdapter(handler))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
