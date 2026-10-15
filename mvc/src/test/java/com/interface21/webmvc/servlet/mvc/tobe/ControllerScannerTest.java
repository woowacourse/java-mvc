package com.interface21.webmvc.servlet.mvc.tobe;

import org.junit.jupiter.api.Test;
import samples.TestController;

import static org.assertj.core.api.Assertions.assertThat;

class ControllerScannerTest {

    @Test
    void createsAnnotatedControllers() {
        final var controllers = new ControllerScanner("samples").scan();

        assertThat(controllers).containsOnlyKeys(TestController.class);
        assertThat(controllers.get(TestController.class)).isInstanceOf(TestController.class);
    }
}
