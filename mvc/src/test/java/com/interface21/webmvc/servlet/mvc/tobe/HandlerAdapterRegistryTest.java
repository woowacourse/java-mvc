package com.interface21.webmvc.servlet.mvc.tobe;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class HandlerAdapterRegistryTest {

    @Test
    void 핸들러를_지원하는_HandlerAdapter를_반환한다() {
        final var handlerExecution = mock(HandlerExecution.class);

        final var registry = new HandlerAdapterRegistry();
        registry.addHandlerAdapter(new HandlerExecutionHandlerAdapter());

        assertThat(registry.getHandlerAdapter(handlerExecution))
                .isInstanceOf(HandlerExecutionHandlerAdapter.class);
    }

    @Test
    void 핸들러를_지원하는_HandlerAdapter가_없으면_예외를_던진다() {
        final var registry = new HandlerAdapterRegistry();
        registry.addHandlerAdapter(new HandlerExecutionHandlerAdapter());

        assertThatThrownBy(() -> registry.getHandlerAdapter(new Object()))
                .isInstanceOf(IllegalStateException.class);
    }
}
