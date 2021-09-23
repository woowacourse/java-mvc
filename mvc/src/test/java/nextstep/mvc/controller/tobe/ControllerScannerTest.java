package nextstep.mvc.controller.tobe;

import java.util.Map;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ControllerScannerTest {

    @Test
    void getControllers() {
        ControllerScanner controllerScanner = new ControllerScanner("samples");
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        assertEquals(1, controllers.size());
    }
}
