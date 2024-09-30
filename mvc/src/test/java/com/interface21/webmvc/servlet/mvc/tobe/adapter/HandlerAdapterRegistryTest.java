package com.interface21.webmvc.servlet.mvc.tobe.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HandlerAdapterRegistryTest {

    private final HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();

    @BeforeEach
    void setUp() {
        handlerAdapterRegistry.addHandlerAdapter(new HandlerExecutionAdapter());
    }

    @Test
    void 지원하는_Handler이면_HandlerAdapter을_반환한다() {
        Object handler = mock(HandlerExecution.class);
        HandlerAdapter actual = handlerAdapterRegistry.getHandlerAdapter(handler);

        assertThat(actual).isInstanceOf(HandlerExecutionAdapter.class);
    }

    @Test
    void 지원하지_않는_Handler_타입은_예외가_발생한다() {
        assertThatThrownBy(() -> handlerAdapterRegistry.getHandlerAdapter(UnsupportedHandler.class))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage("지원하는 handler adapter가 없습니다.");
    }

    private static class UnsupportedHandler {
    }
}
