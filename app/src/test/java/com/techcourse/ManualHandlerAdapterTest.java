package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.techcourse.controller.LoginController;
import org.junit.jupiter.api.Test;

class ManualHandlerAdapterTest {

    @Test
    void support() {
        // given
        HandlerAdapter handlerAdapter = new ManualHandlerAdapter();
        Controller controller = new LoginController();

        // when
        boolean actual = handlerAdapter.support(controller);

        // then
        assertThat(actual).isTrue();
    }
}
