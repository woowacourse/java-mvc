package com.techcourse;

import com.techcourse.controller.legacy.RegisterViewController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;

import static org.assertj.core.api.Assertions.assertThat;

class ControllerAdapterTest {

    private ControllerAdapter controllerAdapter = new ControllerAdapter();

    @Test
    void supports() {
        //given
        Controller controller = new RegisterViewController();

        //when
        boolean supports = controllerAdapter.supports(controller);

        //then
        assertThat(supports).isTrue();
    }

    @Test
    void adapt() throws Exception {
        //given
        Controller controller = new RegisterViewController();

        //when, then
        Assertions.assertThat(controllerAdapter.adapt(null, null, controller))
                .isInstanceOf(ModelAndView.class);
    }
}
