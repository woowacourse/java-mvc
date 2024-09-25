package com.interface21.webmvc.servlet.mvc.tobe.adapter;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.handler.HandlerExecution;

@DisplayName("HandlerExecutionHandlerAdapter Test")
class HandlerExecutionHandlerAdapterTest {

    @DisplayName("입력된 handler 객체가 HandlerExecution 인스턴스면 true를 반환한다.")
    @Test
    void supportsTrue() {
        // Given
        final HandlerExecutionHandlerAdapter handlerExecutionHandlerAdapter = new HandlerExecutionHandlerAdapter();
        final HandlerExecution handler = new HandlerExecution(null, null);

        // When
        final boolean supported = handlerExecutionHandlerAdapter.supports(handler);

        // Then
        assertThat(supported).isTrue();
    }

    @DisplayName("입력된 handler 객체가 HandlerExecution 인스턴스가 아니면 false를 반환한다.")
    @Test
    void supportsFalse() {
        // Given
        final HandlerExecutionHandlerAdapter handlerExecutionHandlerAdapter = new HandlerExecutionHandlerAdapter();
        final Controller handler = (final HttpServletRequest request, final HttpServletResponse response) -> "";

        // When
        final boolean supported = handlerExecutionHandlerAdapter.supports(handler);

        // Then
        assertThat(supported).isFalse();
    }
}
