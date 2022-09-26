package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

class ControllerScannerTest {

    @DisplayName("컨트롤러 어노테이션이 붙은 모든 클래스들을 반환한다.")
    @Test
    void getControllers() {
        Reflections samples = new Reflections("samples");
        ControllerScanner controllerScanner = new ControllerScanner(samples);
        assertThat(controllerScanner.getControllers()).hasSize(1);
    }
}
