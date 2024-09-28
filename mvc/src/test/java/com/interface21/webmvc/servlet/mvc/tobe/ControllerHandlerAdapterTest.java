package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.interface21.webmvc.servlet.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.asis.ForwardController;
import com.techcourse.controller.LoginController;
import com.techcourse.controller.LoginViewController;
import com.techcourse.controller.LogoutController;

class ControllerHandlerAdapterTest {

    @Test
    @DisplayName("Controller 인터페이스가 붙은 클래스를 지원한다.")
    void supports() {
        //given
        final HandlerAdapter adapter = new ControllerHandlerAdapter();
        final List<Object> classes = List.of(new LoginController(), new LogoutController(), new LoginViewController(),
                new ForwardController("/"));

        //when && then
        assertThat(classes).isNotEmpty().allMatch(adapter::supports);
    }
}
