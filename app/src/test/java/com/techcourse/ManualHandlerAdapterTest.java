package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;

import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.techcourse.controller.LoginController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ManualHandlerAdapterTest {

    @DisplayName("Controller를 support하고 HandlerExecution은 support하지 않는다")
    @Test
    void supports() {
        ManualHandlerAdapter manualHandlerAdapter = new ManualHandlerAdapter();
        HandlerExecution handlerExecution = mock(HandlerExecution.class);

        assertAll(
                () -> assertThat(manualHandlerAdapter.supports(new LoginController())).isTrue(),
                () -> assertThat(manualHandlerAdapter.supports(handlerExecution)).isFalse()
        );
    }
}
