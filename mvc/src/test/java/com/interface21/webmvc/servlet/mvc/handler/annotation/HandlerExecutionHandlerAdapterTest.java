package com.interface21.webmvc.servlet.mvc.handler.annotation;

import static org.assertj.core.api.Assertions.assertThat;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.handler.HandlerAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerExecutionHandlerAdapterTest {

    private HandlerAdapter handlerAdapter;

    @BeforeEach
    void setUp() {
        handlerAdapter = new HandlerExecutionHandlerAdapter();
    }

    @DisplayName("HandlerExecution 타입 객체를 지원한다.")
    @Test
    void supports_WhenHandlerInstanceOfHandlerAdapter() {
        Object handler = new HandlerExecution(null, null);

        assertThat(handlerAdapter.supports(handler)).isTrue();
    }

    @DisplayName("Controller 타입 객체는 지원하지 않는다.")
    @Test
    void supports_WhenHandlerInstanceOfController() {
        Object handler = (Controller) (req, res) -> "";

        assertThat(handlerAdapter.supports(handler)).isFalse();
    }
}
