package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import samples.TestController;

class ControllerScannerTest {

    @DisplayName("@Controller 가 붙은 클래스를 찾을 수 있다.")
    @Test
    void getControllers() {
        // given
        final ControllerScanner controllerScanner = new ControllerScanner(new Reflections("samples"));

        // when
        final Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        // then
        assertThat(controllers.keySet()).hasSize(1).contains(TestController.class);
        assertThat(controllers.get(TestController.class)).isInstanceOf(TestController.class);
    }
}
