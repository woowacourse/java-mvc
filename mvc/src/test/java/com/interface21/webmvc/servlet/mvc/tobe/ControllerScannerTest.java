package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

class ControllerScannerTest {

    private ControllerScanner controllerScanner;

    @BeforeEach
    void setUp() {
        controllerScanner = new ControllerScanner("samples");
    }

    @DisplayName("컨트롤러 어노테이션이 붙은 컨트롤러들을 찾아 인스턴스를 생성한다.")
    @Test
    void getControllers() {
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        assertAll(
                () -> assertThat(controllers.size()).isEqualTo(1),
                () -> assertThat(controllers.get(TestController.class)).isInstanceOf(TestController.class)
        );
    }
}
