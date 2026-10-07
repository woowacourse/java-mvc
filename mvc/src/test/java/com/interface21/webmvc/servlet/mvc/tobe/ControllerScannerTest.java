package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.Test;
import samples.TestController;

class ControllerScannerTest {

    @Test
    void getControllers_ThenReturnInstancesOfControllerAnnotatedClasses() {
        final Object[] basePackage = new String[]{"samples"};
        final ControllerScanner controllerScanner = new ControllerScanner(basePackage);

        final Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        assertThat(controllers).containsKey(TestController.class);
        assertThat(controllers.get(TestController.class)).isInstanceOf(TestController.class);
    }
}
