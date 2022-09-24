package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

class ControllerScannerTest {

    @DisplayName("패키지내의 @Controller가 붙은 객체들을 스캔한다.")
    @Test
    void scan() {
        final Reflections reflections = new Reflections("samples");
        final ControllerScanner controllerScanner = new ControllerScanner(reflections);
        final Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        assertThat(controllers.size()).isEqualTo(1);
    }
}
