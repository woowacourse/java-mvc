package com.interface21.webmvc.servlet.mvc.tobe;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.NotController;
import samples.TestController;

import static org.assertj.core.api.Assertions.assertThat;

public class ControllerScanTest {

    @Test
    @DisplayName("@Controller 어노테이션이 붙은 클래스를 찾을 수 있다.")
    void scan_controller() {
        ControllerScanner controllerScanner = new ControllerScanner("samples");
        assertThat(controllerScanner.controllers).containsKey(TestController.class);
    }

    @Test
    @DisplayName("@Controller 어노테이션이 붙지 않은 클래스는 스캔하지 않는다.")
    void noScan_notController() {
        ControllerScanner controllerScanner = new ControllerScanner("samples");
        assertThat(controllerScanner.controllers).doesNotContainKey(NotController.class);
    }
}
