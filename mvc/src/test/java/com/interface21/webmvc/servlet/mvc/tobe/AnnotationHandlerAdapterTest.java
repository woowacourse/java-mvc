package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.interface21.context.stereotype.Controller;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AnnotationHandlerAdapterTest {

    @Test
    @DisplayName("HandlerExecution을 처리할 수 있다.")
    void should_return_true_when_canhandle_HandlerExecution() {
        // given
        HandlerExecution mockHandlerExecution = mock(HandlerExecution.class);
        HandlerAdapter handlerAdapter = new AnnotationHandlerAdapter();

        // when & then
        assertThat(handlerAdapter.canHandle(mockHandlerExecution)).isTrue();
    }

    @Test
    @DisplayName("Controller를 처리할 수 없다.")
    void should_return_true_when_canhandle_HandlerExecution2() {
        // given
        Controller mockController = mock(Controller.class);
        HandlerAdapter handlerAdapter = new AnnotationHandlerAdapter();

        // when & then
        assertThat(handlerAdapter.canHandle(mockController)).isFalse();
    }
}
