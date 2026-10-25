package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import samples.TestController;

class ControllerScannerTest {

    @Test
    void scanControllersAnnotatedWithController() {
        final var controllerScanner = new ControllerScanner("samples");

        final var controllers = controllerScanner.getControllers();

        assertThat(controllers).containsOnlyKeys(TestController.class);
        assertThat(controllers.get(TestController.class)).isInstanceOf(TestController.class);
    }
}
