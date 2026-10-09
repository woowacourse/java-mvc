package com.interface21.webmvc.servlet.mvc.tobe;

import org.junit.jupiter.api.Test;
import samples.MappingController;
import samples.StatefulController;
import samples.TestController;
import scannerfixtures.unconstructable.NoDefaultConstructorController;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ControllerScannerTest {

    @Test
    void createsOnlyAnnotatedControllersInTheConfiguredPackage() {
        final var scanner = new ControllerScanner("samples");

        final var controllers = scanner.scan();

        assertThat(controllers.keySet()).containsExactlyInAnyOrder(
                MappingController.class, StatefulController.class, TestController.class);
        controllers.forEach((controllerClass, target) -> assertThat(target).isInstanceOf(controllerClass));
    }

    @Test
    void createsFreshControllersForEachScan() {
        final var scanner = new ControllerScanner("samples");

        final var first = scanner.scan();
        final var second = scanner.scan();

        first.forEach((controllerClass, target) -> assertThat(second.get(controllerClass)).isNotSameAs(target));
    }

    @Test
    void preservesControllerClassAndCauseWhenConstructionFails() {
        final var scanner = new ControllerScanner("scannerfixtures.unconstructable");

        assertThatThrownBy(scanner::scan)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Failed to create controller: " + NoDefaultConstructorController.class.getName())
                .hasCauseInstanceOf(NoSuchMethodException.class);
    }
}
