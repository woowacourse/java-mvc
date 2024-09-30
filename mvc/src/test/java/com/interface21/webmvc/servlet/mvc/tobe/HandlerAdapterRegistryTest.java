package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import samples.TestController;

class HandlerAdapterRegistryTest {

    @Test
    @DisplayName("핸들러를 처리할 수 있는 핸들러 어댑터를 반환한다.")
    void return_handler_adapter() {
        // given
        final var registry = new HandlerAdapterRegistry(List.of(new AnnotationHandlerAdapter()));
        HandlerExecution handlerExecution = new HandlerExecution(new TestController(), null);

        // when
        final HandlerAdapter handlerAdapter = registry.getHandlerAdapter(handlerExecution);

        // then
        assertThat(handlerAdapter).isInstanceOf(AnnotationHandlerAdapter.class);
    }

    @Test
    @DisplayName("핸들러를 처리할 수 있는 핸들러 어댑터가 없다면 예외가 발생한다.")
    void throw_exception_when_cant_handle() {
        // given
        final var registry = new HandlerAdapterRegistry(List.of());
        HandlerExecution handlerExecution = new HandlerExecution(new TestController(), null);

        // when & then
        assertThatThrownBy(() -> registry.getHandlerAdapter(handlerExecution))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
