package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.OngoingStubbing;
import samples.TestController;

class ViewNameHandlerExecutionAdapterTest {

    @Test
    @DisplayName("ViewName을 반환하는 HandlerExecution 타입만 지원한다.")
    void supports() {
        ViewNameHandlerExecutionAdapter viewNameHandlerExecutionAdapter = new ViewNameHandlerExecutionAdapter();
        HandlerExecution handlerExecution = mock(HandlerExecution.class);
        Class<String> stringClass = String.class;
        OngoingStubbing<Class<?>> ongoingStubbing = when(handlerExecution.getReturnType());
        ongoingStubbing.thenReturn(stringClass);
        TestController otherController = new TestController();

        Assertions.assertAll(
                () -> assertThat(viewNameHandlerExecutionAdapter.supports(handlerExecution)).isTrue(),
                () -> assertThat(viewNameHandlerExecutionAdapter.supports(otherController)).isFalse()
        );
    }
}
