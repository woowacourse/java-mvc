package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

class ControllerScannerTest {

    @DisplayName("Controller 어노테이션이 붙은 클래스를 인스턴스화한다.")
    @Test
    void initialize() {
        // given
        ControllerScanner controllerScanner = new ControllerScanner(new Object[]{"samples"});
        // when && then
        assertAll(
                () -> assertThat(controllerScanner.getControllers()).containsKey(TestController.class),
                () -> assertThat(controllerScanner.getControllers()).hasSize(1)
        );
    }
}
