package com.interface21.webmvc.servlet.mvc.tobe;

import fixtures.scanning.valid.ScannedController;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ControllerScannerTest {

    @Test
    void createsOnlyAnnotatedControllersInBasePackage() {
        final var controllers = new ControllerScanner("fixtures.scanning.valid").getControllers();

        assertThat(controllers).containsOnlyKeys(ScannedController.class);
        assertThat(controllers.get(ScannedController.class)).isInstanceOf(ScannedController.class);
    }

    @Test
    void reportsControllerThatCannotBeInstantiated() {
        final var scanner = new ControllerScanner("fixtures.scanning.invalid");

        assertThatThrownBy(scanner::getControllers)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("MissingDefaultConstructorController")
                .hasCauseInstanceOf(NoSuchMethodException.class);
    }
}
