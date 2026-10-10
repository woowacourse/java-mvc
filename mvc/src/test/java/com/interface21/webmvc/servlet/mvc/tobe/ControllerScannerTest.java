package com.interface21.webmvc.servlet.mvc.tobe;

import org.junit.jupiter.api.Test;
import samples.TestController;

import static org.assertj.core.api.Assertions.assertThat;

class ControllerScannerTest {

    @Test
    void scansAndInstantiatesOnlyAnnotatedControllers() {
        final var scanner = new ControllerScanner("samples");

        final var controllers = scanner.scan();

        assertThat(controllers).containsOnlyKeys(TestController.class);
        assertThat(controllers.get(TestController.class)).isInstanceOf(TestController.class);
    }

    @Test
    void returnsEmptyMapWhenPackageContainsNoControllers() {
        final var scanner = new ControllerScanner("com.interface21.web.bind.annotation");

        assertThat(scanner.scan()).isEmpty();
    }
}
