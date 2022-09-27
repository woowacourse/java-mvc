package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import samples.TestController;

class ControllerScannerTest {

    private ControllerScanner controllerScanner;

    @BeforeEach
    void setUp() {
        controllerScanner = new ControllerScanner(new Reflections("samples"));
    }

    @Test
    void Controller_어노테이션이_붙은_클래스를_조회한다() {
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        Object instance = controllers.get(TestController.class);

        assertThat(instance).isNotNull();
    }
}
