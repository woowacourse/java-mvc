package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Map;
import org.junit.jupiter.api.Test;
import samples.TestController;

class ControllerScannerTest {

    @Test
    void 컨트롤러를_찾아서_인스턴스_생성한다() {
        ControllerScanner controllerScanner = new ControllerScanner("samples");
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        assertAll(
                () -> assertThat(controllers).size().isEqualTo(1),
                () -> assertThat(controllers.get(TestController.class)).isInstanceOf(TestController.class)
        );
    }
}
