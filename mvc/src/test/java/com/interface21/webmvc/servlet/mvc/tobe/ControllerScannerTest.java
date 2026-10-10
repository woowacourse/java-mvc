package com.interface21.webmvc.servlet.mvc.tobe;

import org.junit.jupiter.api.Test;
import samples.TestController;

import static org.assertj.core.api.Assertions.assertThat;

class ControllerScannerTest {

    @Test
    void createsAnnotatedController() {
        final var controllerScanner = new ControllerScanner("samples");

        final var controllers = controllerScanner.getControllers();

        assertThat(controllers).containsOnlyKeys(TestController.class);
        assertThat(controllers.get(TestController.class)).isInstanceOf(TestController.class);
    }

    @Test
    void excludesClassesWithoutControllerAnnotation() {
        final var controllerScanner = new ControllerScanner("com.interface21.webmvc.servlet");

        final var controllers = controllerScanner.getControllers();

        assertThat(controllers).isEmpty();
    }
}
