package nextstep.mvc.controller;

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
    void getController() {
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        assertThat(controllers.get(TestController.class)).isInstanceOf(TestController.class);
    }
}
