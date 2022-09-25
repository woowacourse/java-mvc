package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

class ControllerScannerTest {

    private final ControllerScanner controllerScanner = new ControllerScanner("samples");

    @Test
    @DisplayName("해당 패키지의 인스턴스화된 컨트롤러들을 가져올 수 있다.")
    void getControllers() {
        final Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        assertThat(controllers.keySet()).contains(TestController.class);
    }
}