package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AnnotationHandlerMappingAdapterTest {

    @Test
    @DisplayName("HandlerExecution을 처리할 수 있다.")
    void should_return_true_when_canhandle_HandlerExecution() {
        // given
        HandlerExecution mockHandlerExecution = mock(HandlerExecution.class);
        HandlerAdapter handlerAdapter = new AnnotationHandlerMappingAdapter();

        // when & then
        assertThat(handlerAdapter.canHandle(mockHandlerExecution)).isTrue();
    }
}
