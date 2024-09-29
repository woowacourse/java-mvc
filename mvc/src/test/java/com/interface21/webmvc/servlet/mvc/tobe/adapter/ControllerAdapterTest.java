package com.interface21.webmvc.servlet.mvc.tobe.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import org.junit.jupiter.api.Test;

class ControllerAdapterTest {

    @Test
    void Controller_타입_핸들러이면_true를_반환한다() {
        ControllerAdapter controllerAdapter = new ControllerAdapter();

        Controller supportedHandler = mock(Controller.class);

        assertThat(controllerAdapter.supports(supportedHandler)).isTrue();
    }

    @Test
    void Controller_타입_핸들러가_아니면_false를_반환한다() {
        ControllerAdapter controllerAdapter = new ControllerAdapter();

        HandlerExecution unsupportedHandler = mock(HandlerExecution.class);

        assertThat(controllerAdapter.supports(unsupportedHandler)).isFalse();
    }
}
