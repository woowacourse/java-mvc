package nextstep.mvc.controller;

import static org.assertj.core.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ControllerScannerTest {

    @Test
    @DisplayName("어노테이션이 붙은 controller를 가져온다.")
    void getAnnotatedControllers() {
        ControllerScanner controllerScanner = new ControllerScanner("samples");

        Map<Class<?>, Object> annotatedControllers = controllerScanner.getAnnotatedControllers();

        assertThat(annotatedControllers).hasSize(1);
    }
}
