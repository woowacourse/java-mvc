package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Map;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import samples.TestController;

class ControllerScannerTest {

    @Test
    void getControllers() {
        ControllerScanner controllerScanner = new ControllerScanner(new Reflections("samples"));
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        assertAll(
                () -> assertThat(controllers).containsKey(TestController.class),
                () -> assertThat(controllers.get(TestController.class)).isNotNull()
        );
    }
}
