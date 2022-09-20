package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.controller.tobe.HandlerExecution;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ManualHandlerAdapterTest {

    private static final HandlerAdapter HANDLER_ADAPTER = new ManualHandlerAdapter();

    @DisplayName("handle 할 수 있는 handler인지 반환한다 (true)")
    @Test
    void supports_True() {
        Controller controller = Mockito.mock(Controller.class);

        assertThat(HANDLER_ADAPTER.supports(controller)).isTrue();
    }

    @DisplayName("handle 할 수 있는 handler인지 반환한다 (false)")
    @Test
    void supports_False() {
        HandlerExecution handlerExecution = Mockito.mock(HandlerExecution.class);

        assertThat(HANDLER_ADAPTER.supports(handlerExecution)).isFalse();
    }
}
