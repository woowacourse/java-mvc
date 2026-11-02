package com.interface21.webmvc.servlet.mvc.scanner;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.Test;
import samples.TestController;

class ControllerScannerTest {

    @Test
    void findsAndInstantiatesControllersInBasePackage() {
        final Map<Class<?>, Object> controllers = ControllerScanner.getControllers("samples");

        assertThat(controllers)
            .containsKey(TestController.class);
        assertThat(controllers.get(TestController.class))
            .isInstanceOf(TestController.class);
    }
}
