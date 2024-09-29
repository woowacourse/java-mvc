package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.adapter.HandlerExecutionAdapter;
import com.techcourse.controller.RegisterController;
import com.techcourse.controller.RegisterViewController;
import org.junit.jupiter.api.Test;

class HandlerExecutionAdapterTest {

    private final HandlerExecutionAdapter handlerExecutionAdapter = new HandlerExecutionAdapter();

    @Test
    void HandlerExcution_타입_핸들러이면_true를_반환한다() {
        HandlerExecution supportedHandler = new HandlerExecution(new RegisterController(),
                RegisterController.class.getMethods()[0]);

        assertThat(handlerExecutionAdapter.supports(supportedHandler)).isTrue();
    }

    @Test
    void HandlerExcution_타입_핸들러가_아니면_false를_반환한다() {
        Controller unsupportedHandler = new RegisterViewController();

        assertThat(handlerExecutionAdapter.supports(unsupportedHandler)).isFalse();
    }
}
