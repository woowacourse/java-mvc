package com.interface21.webmvc.servlet.mvc.tobe.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import org.junit.jupiter.api.Test;

class HandlerExecutionAdapterTest {

    @Test
    void HandlerExcution_타입_핸들러이면_true를_반환한다() {
        HandlerExecutionAdapter handlerExecutionAdapter = new HandlerExecutionAdapter();

        HandlerExecution supportedHandler = mock(HandlerExecution.class);

        assertThat(handlerExecutionAdapter.supports(supportedHandler)).isTrue();
    }

    @Test
    void HandlerExcution_타입_핸들러가_아니면_false를_반환한다() {
        HandlerExecutionAdapter handlerExecutionAdapter = new HandlerExecutionAdapter();

        Controller unsupportedHandler = mock(Controller.class);

        assertThat(handlerExecutionAdapter.supports(unsupportedHandler)).isFalse();
    }
}
