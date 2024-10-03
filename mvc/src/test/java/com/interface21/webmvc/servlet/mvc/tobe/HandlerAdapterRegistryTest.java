package com.interface21.webmvc.servlet.mvc.tobe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.interface21.webmvc.servlet.mvc.tobe.adapter.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.adapter.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.adapter.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.mvc.tobe.handler.HandlerExecution;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class HandlerAdapterRegistryTest {

    private HandlerAdapterRegistry handlerAdapterRegistry;

    @BeforeEach
    void setUp() {
        handlerAdapterRegistry = new HandlerAdapterRegistry();
        AnnotationHandlerAdapter annotationHandlerAdapter = new AnnotationHandlerAdapter();
        handlerAdapterRegistry.addHandlerAdapter(annotationHandlerAdapter);
    }

    @Test
    @DisplayName("handler를 통해 handlerAdapter를 찾을 수 있다.")
    void getHandlerAdapter() {
        // given
        HandlerExecution handlerExecution = mock(HandlerExecution.class);
        HandlerAdapter mockAdapter = handlerAdapterRegistry.getHandlerAdapter(handlerExecution);

        // then
        assertThat(mockAdapter).isNotNull();
    }

    @Test
    @DisplayName("특정 handler를 처리할 수 있는 handlerAdapter가 없는 경우 예외가 발생한다.")
    void failGetHandlerAdapter() {
        // given
        String handler = "handler";

        // then
        assertThatThrownBy(() -> handlerAdapterRegistry.getHandlerAdapter(handler))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("핸들러를 처리할 핸들러 어댑터가 존재하지 않습니다 " + handler);
    }
}

