package com.interface21.webmvc.servlet.mvc.tobe.handleradaptor;

import static org.assertj.core.api.Assertions.assertThat;

import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerExecutionHandlerAdapterTest {

    @DisplayName("HandlerExecution을 지원한다.")
    @Test
    void supportTrue() {
        HandlerExecution handlerExecution = new HandlerExecution(getClass().getMethods()[0]);
        HandlerExecutionHandlerAdapter handlerAdapter = new HandlerExecutionHandlerAdapter();

        boolean support = handlerAdapter.support(handlerExecution);

        assertThat(support).isTrue();
    }

    @DisplayName("HandlerExecution이 아니면 지원하지 않는다.")
    @Test
    void supportFalse() {
        Object object = new Object();
        HandlerExecutionHandlerAdapter handlerAdapter = new HandlerExecutionHandlerAdapter();

        boolean support = handlerAdapter.support(object);

        assertThat(support).isFalse();
    }
}
