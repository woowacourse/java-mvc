package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import samples.TestController;

class ControllerScannerTest {

    @DisplayName("@Controller 붙은 클래스, 인스턴스를 반환한다.")
    @Test
    void getControllers() {
        ControllerScanner controllerScanner = new ControllerScanner(new Reflections("samples"));
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        assertAll(
                () -> assertThat(controllers).hasSize(1),
                () -> assertThat(controllers.values().toArray()[0]).isInstanceOf(TestController.class)
        );
    }
}
