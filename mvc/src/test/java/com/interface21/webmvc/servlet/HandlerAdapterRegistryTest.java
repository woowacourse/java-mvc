package com.interface21.webmvc.servlet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.annotation.AnnotationHandlerAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerAdapterRegistryTest {

    private HandlerAdapterRegistry handlerAdapterRegistry;

    @BeforeEach
    void setUp() {
        handlerAdapterRegistry = new HandlerAdapterRegistry();
        handlerAdapterRegistry.initialize();
    }

    @DisplayName("HandlerExecution 타입의 핸들러의 경우 AnnotationHandlerAdapter를 반환한다.")
    @Test
    void should_getAnnotationHandlerAdapter_when_givenHandlerExecution() {
        // given
        Object handler = new HandlerExecution(null, null);

        // when
        HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler);

        // then
        assertThat(handlerAdapter).isInstanceOf(AnnotationHandlerAdapter.class);
    }

    @DisplayName("HandlerExecution 타입이 아닌 핸들러의 경우 예외가 발생한다.")
    @Test
    void should_throwException_when_givenNotHandlerExecution() {
        // given
        Object handler = new Object();

        // when & then
        assertThatThrownBy(() -> handlerAdapterRegistry.getHandlerAdapter(handler))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 핸들러를 수행하는 어댑터가 없습니다.");
    }
}
