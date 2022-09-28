package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import samples.TestController;

@DisplayName("ControllerScanner 는 ")
class ControllerScannerTest {

    @DisplayName("컨트롤러 목록을 가져온다.")
    @Test
    void getControllers() {
        final Reflections reflections = new Reflections("samples");
        final ControllerScanner controllerScanner = new ControllerScanner(reflections);

        final Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        assertThat(controllers.keySet()).isEqualTo(Set.of(TestController.class));
    }
}
