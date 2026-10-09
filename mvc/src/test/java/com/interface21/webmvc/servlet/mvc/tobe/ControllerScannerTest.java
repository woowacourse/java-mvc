package com.interface21.webmvc.servlet.mvc.tobe;

import org.junit.jupiter.api.Test;
import samples.TestController;

import static org.assertj.core.api.Assertions.assertThat;

class ControllerScannerTest {

    @Test
    void scansAndCreatesControllersAnnotatedWithController() {
        final var controllerScanner = new ControllerScanner("samples");

        final var controllers = controllerScanner.scan();

        assertThat(controllers)
                .containsKey(TestController.class);
        assertThat(controllers.get(TestController.class))
                .isInstanceOf(TestController.class);
    }
}
