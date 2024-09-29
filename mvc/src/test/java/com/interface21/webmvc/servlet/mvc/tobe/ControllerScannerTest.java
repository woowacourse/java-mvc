package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;
import samples2.TestController2;

class ControllerScannerTest {

    @DisplayName("주어진 basePackage에 존재하는 컨트롤러를 스캔할 수 있다.")
    @Test
    void controllerScan() {
        ControllerScanner controllerScanner = new ControllerScanner("samples");
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        Object testController = controllers.get(TestController.class);

        assertThat(controllers.size()).isEqualTo(1);
        assertThat(testController).isNotNull();
    }

    @DisplayName("여러 basePackage에 존재하는 컨트롤러를 스캔할 수 있다.")
    @Test
    void multiplePackageControllerScan() {
        ControllerScanner controllerScanner = new ControllerScanner("samples", "samples2");
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        Object testController = controllers.get(TestController.class);
        Object testController2 = controllers.get(TestController2.class);

        assertThat(controllers.size()).isEqualTo(2);
        assertThat(testController).isNotNull();
        assertThat(testController2).isNotNull();
    }
}
