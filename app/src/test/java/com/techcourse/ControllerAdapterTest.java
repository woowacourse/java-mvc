package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.adapter.ControllerAdapter;
import com.techcourse.controller.RegisterController;
import com.techcourse.controller.RegisterViewController;
import org.junit.jupiter.api.Test;

class ControllerAdapterTest {

    private final ControllerAdapter controllerAdapter = new ControllerAdapter();

    @Test
    void Controller_타입_핸들러이면_true를_반환한다() {
        Controller supportedHandler = new RegisterViewController();

        assertThat(controllerAdapter.supports(supportedHandler)).isTrue();
    }

    @Test
    void Controller_타입_핸들러가_아니면_false를_반환한다() {
        HandlerExecution unsupportedHandler = new HandlerExecution(new RegisterController(),
                RegisterController.class.getMethods()[0]);

        assertThat(controllerAdapter.supports(unsupportedHandler)).isFalse();
    }
}
