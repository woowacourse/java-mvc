package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.Test;
import samples.TestController;

class ControllerScannerTest {

    @Test
    void controllerScan () {
        ControllerScanner controllerScanner = new ControllerScanner("samples");

        Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        assertThat(controllers.keySet()).contains(TestController.class);
    }
}
