package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

class ControllerScannerTest {

    @DisplayName("@Controller-annotated classes의 인스턴스 생성")
    @Test
    void getControllers() {
        ControllerScanner controllerScanner = new ControllerScanner("samples");
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        assertAll(
                () -> assertThat(controllers).containsOnlyKeys(TestController.class),
                () -> assertThat(controllers.values()).hasOnlyElementsOfTypes(TestController.class)
        );
        System.out.println("controllers = " + controllers.size());
    }
}
