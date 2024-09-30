package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.interface21.webmvc.servlet.HandlerAdapter;

class HandlerExecutionHandlerAdapterTest {

    @Test
    @DisplayName("handlerExecution을 지원한다.")
    void supports() {
        //given
        final HandlerExecution handlerExecution = mock(HandlerExecution.class);
        final HandlerAdapter adapter = new HandlerExecutionHandlerAdapter();

        //when && then
        assertThat(adapter.supports(handlerExecution)).isTrue();
    }
}
