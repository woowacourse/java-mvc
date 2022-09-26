package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

class ControllerScannerTest {

    @Test
    @DisplayName("컨트롤러를 찾는다")
    void getController() {
        final Reflections reflections = new Reflections("samples");
        final ControllerScanner controllerScanner = new ControllerScanner(reflections);

        final Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        assertThat(controllers).hasSize(1);
    }
}
