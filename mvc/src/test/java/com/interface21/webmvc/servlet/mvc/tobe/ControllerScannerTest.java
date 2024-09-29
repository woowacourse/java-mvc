package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.Test;

class ControllerScannerTest {

    @Test
    void 지정된_basePackages에_존재하는_Controller_애노테이션을_가진_클래스를_조회한다() {
        ControllerScanner controllerScanner = new ControllerScanner(
                new Object[]{"com.interface21.webmvc.servlet.mvc.tobe"});
        Map<Class<?>, Object> scannedController = controllerScanner.scan();

        assertThat(scannedController.keySet()).contains(ControllerScanTestController.class);
    }
}
