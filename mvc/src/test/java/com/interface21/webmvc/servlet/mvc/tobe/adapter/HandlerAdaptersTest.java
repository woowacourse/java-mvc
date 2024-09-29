package com.interface21.webmvc.servlet.mvc.tobe.adapter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.interface21.webmvc.servlet.mvc.tobe.handler.HandlerExecution;

@DisplayName("HandlerAdapters Test")
class HandlerAdaptersTest {

    @DisplayName("HandlerExecution 타입 handler를 입력받으면 handler를 처리할 수 있는 HandlerExecutionHandlerAdapter를 찾아 반환한다.")
    @Test
    void getHandlerAdapterForHandlerExecutionHandlerAdapter() {
        // Given
        final HandlerAdapters handlerAdapters = new HandlerAdapters();
        final HandlerExecution handler = new HandlerExecution(null, null);

        // When
        final HandlerAdapter handlerAdapter = handlerAdapters.getHandlerAdapter(handler);

        // Then
        final boolean isHandlerExecutionHandlerAdapter = handlerAdapter instanceof HandlerExecutionHandlerAdapter;
        assertThat(isHandlerExecutionHandlerAdapter).isTrue();
    }
}
