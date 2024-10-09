package com.interface21.webmvc.servlet.mvc.tobe.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class HandlerAdapterRegistryTest {

    private HandlerAdapterRegistry handlerAdapterRegistry;

    @BeforeEach
    void setUp() {
        handlerAdapterRegistry = new HandlerAdapterRegistry();

        handlerAdapterRegistry.addHandlerAdapter(new HandlerExecutionHandlerAdapter());
    }

    @DisplayName("handler에 맞는 handlerAdapter를 반환한다.")
    @Nested
    class getHandlerAdapter {

        @Test
        void getHandlerExecutionHandlerAdapter() {
            Object handler = mock(HandlerExecution.class);

            HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler);

            assertThat(handlerAdapter).isInstanceOf(HandlerExecutionHandlerAdapter.class);
        }
    }

    @DisplayName("요청에 맞는 handlerAdapter가 없으면 예외가 발생한다.")
    @Test
    void getHandlerAdapterThrowException() {
        HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();
        Object handler = new Object();

        assertThatThrownBy(() -> handlerAdapterRegistry.getHandlerAdapter(handler))
                .isInstanceOf(NoSuchElementException.class);
    }
}
