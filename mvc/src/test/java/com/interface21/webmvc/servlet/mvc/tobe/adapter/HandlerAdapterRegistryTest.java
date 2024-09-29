package com.interface21.webmvc.servlet.mvc.tobe.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class HandlerAdapterRegistryTest {

    private final HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();

    @BeforeEach
    void setUp() {
        handlerAdapterRegistry.addHandlerAdapter(new ControllerAdapter());
        handlerAdapterRegistry.addHandlerAdapter(new HandlerExecutionAdapter());
    }

    @ParameterizedTest
    @MethodSource("supportedHandlerAndAdapter")
    void 지원하는_Handler이면_HandlerAdapter을_반환한다(Class<?> handlerClass, Class<?> expectedAdapterClass) {
        Object handler = mock(handlerClass);
        HandlerAdapter actual = handlerAdapterRegistry.getHandlerAdapter(handler);

        assertThat(actual).isInstanceOf(expectedAdapterClass);
    }

    static Stream<Arguments> supportedHandlerAndAdapter() {
        return Stream.of(
                Arguments.arguments(HandlerExecution.class, HandlerExecutionAdapter.class),
                Arguments.arguments(Controller.class, ControllerAdapter.class)
        );
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
