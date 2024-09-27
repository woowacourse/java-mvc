package com.interface21.webmvc.servlet.mvc.asis;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;
import samples.TestControllerInterfaceController;

class ControllerHandlerAdapterTest {

    @Test
    @DisplayName("Controller 인터페이스의 하위 타입을 지원한다.")
    void supports() {
        ControllerHandlerAdapter controllerHandlerAdapter = new ControllerHandlerAdapter();
        Controller controller = new TestControllerInterfaceController();
        TestController otherController = new TestController();

        Assertions.assertAll(
                () -> assertThat(controllerHandlerAdapter.supports(controller)).isTrue(),
                () -> assertThat(controllerHandlerAdapter.supports(otherController)).isFalse()
        );
    }
}
