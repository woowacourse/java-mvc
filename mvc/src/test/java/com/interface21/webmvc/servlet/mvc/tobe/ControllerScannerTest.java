package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.interface21.context.stereotype.Controller;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ControllerScannerTest {

    @DisplayName("@Controller-annotated classes의 인스턴스 생성")
    @Test
    void getControllers() {
        String currentPackage = getClass().getPackage().getName();
        ControllerScanner controllerScanner = new ControllerScanner(currentPackage);
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        assertAll(
                () -> assertThat(controllers).containsOnlyKeys(AController.class, BController.class),
                () -> assertThat(controllers.values()).hasOnlyElementsOfTypes(AController.class, BController.class)
        );
        System.out.println("controllers = " + controllers.size());
    }

    @Controller
    static class AController {

    }

    @Controller
    static class BController {

    }
}
