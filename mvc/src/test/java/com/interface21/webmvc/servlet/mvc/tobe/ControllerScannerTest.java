package com.interface21.webmvc.servlet.mvc.tobe;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import samples.TestController;

import static org.assertj.core.api.Assertions.assertThat;

class ControllerScannerTest {

    @Test
    @DisplayName("Controller 어노테이션이 붙은 클래스를 탐색하고 인스턴스를 생성한다")
    void scansAndCreatesControllersAnnotatedWithController() {
        final var controllerScanner = new ControllerScanner("samples");

        final var controllers = controllerScanner.scan();

        assertThat(controllers)
                .containsKey(TestController.class);
        assertThat(controllers.get(TestController.class))
                .isInstanceOf(TestController.class);
    }
}
