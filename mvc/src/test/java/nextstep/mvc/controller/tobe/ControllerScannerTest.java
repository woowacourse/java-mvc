package nextstep.mvc.controller.tobe;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ControllerScannerTest {

    @Test
    @DisplayName("Controller 어노테이션이 분은 클래스를 찾을 수 있다.")
    void redirect() {
        final ControllerScanner controllerScanner = ControllerScanner.from("samples");

        Set<Class<?>> annotationControllers = controllerScanner.getControllers();

        assertThat(annotationControllers).hasSize(1);
    }
}
