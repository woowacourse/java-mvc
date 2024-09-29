package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.adapter.ControllerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.adapter.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.adapter.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.mvc.tobe.adapter.HandlerExecutionAdapter;
import com.techcourse.controller.RegisterController;
import com.techcourse.controller.RegisterViewController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HandlerAdapterRegistryTest {

    private final HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();

    @BeforeEach
    void setUp() {
        handlerAdapterRegistry.addHandlerAdapter(new ControllerAdapter());
        handlerAdapterRegistry.addHandlerAdapter(new HandlerExecutionAdapter());
    }

    @Test
    void 파라미터_타입이_HandlerExcution이면_HandlerExecutionAdapter를_반환한다() {
        HandlerExecution handler = new HandlerExecution(new RegisterController(),
                RegisterController.class.getMethods()[0]);

        HandlerAdapter actual = handlerAdapterRegistry.getHandlerAdapter(handler);

        assertThat(actual).isInstanceOf(HandlerExecutionAdapter.class);
    }

    @Test
    void 파라미터_타입이_Controller이면_ControllerAdapter를_반환한다() {
        Controller handler = new RegisterViewController();

        HandlerAdapter actual = handlerAdapterRegistry.getHandlerAdapter(handler);

        assertThat(actual).isInstanceOf(ControllerAdapter.class);
    }

    @Test
    void 지원하지_않는_Handler_타입은_예외가_발생한다() {
        assertThatThrownBy(() -> handlerAdapterRegistry.getHandlerAdapter(InvalidHandler.class))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage("지원하는 handler adapter가 없습니다.");
    }

    private static class InvalidHandler {
    }
}
