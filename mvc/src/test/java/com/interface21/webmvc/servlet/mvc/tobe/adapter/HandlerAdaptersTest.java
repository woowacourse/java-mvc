package com.interface21.webmvc.servlet.mvc.tobe.adapter;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.handler.HandlerExecution;

@DisplayName("HandlerAdapters Test")
class HandlerAdaptersTest {

    @DisplayName("Controller 타입 handler를 입력받으면 handler를 처리할 수 있는 ControllerHandlerAdapter를 찾아 반환한다.")
    @Test
    void getHandlerAdapterForControllerHandlerAdapter() {
        // Given
        final HandlerAdapters handlerAdapters = new HandlerAdapters();
        final Controller handler = (final HttpServletRequest req, final HttpServletResponse res) -> "";

        // When
        final HandlerAdapter handlerAdapter = handlerAdapters.getHandlerAdapter(handler);

        // Then
        final boolean isControllerHandlerAdapter = handlerAdapter instanceof ControllerHandlerAdapter;
        assertThat(isControllerHandlerAdapter).isTrue();
    }

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
