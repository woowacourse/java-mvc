package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

class HandlerExecutionAdapterTest {

    @Test
    @DisplayName("HandlerExecution 타입만 지원한다.")
    void supports() {
        HandlerExecutionAdapter controllerHandlerAdapter = new HandlerExecutionAdapter();
        HandlerExecution handlerExecution = mock(HandlerExecution.class);
        TestController otherController = new TestController();

        Assertions.assertAll(
                () -> assertThat(controllerHandlerAdapter.supports(handlerExecution)).isTrue(),
                () -> assertThat(controllerHandlerAdapter.supports(otherController)).isFalse()
        );
    }
}
