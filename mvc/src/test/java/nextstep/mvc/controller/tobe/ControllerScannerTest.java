package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import nextstep.mvc.controller.ControllerScanner;
import org.junit.jupiter.api.Test;
import samples.TestController;

class ControllerScannerTest {

    @Test
    void getControllers() {
        ControllerScanner controllerScanner = ControllerScanner.from("samples");
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        Object controller = controllers.get(TestController.class);

        assertThat(controller).isInstanceOf(TestController.class);
    }
}
