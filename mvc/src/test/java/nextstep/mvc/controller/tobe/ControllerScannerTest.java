package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import samples.TestController;

class ControllerScannerTest {

    @Test
    void getControllers() {
        ControllerScanner controllerScanner = new ControllerScanner("samples");

        List<Object> controllers = controllerScanner.getControllers();
        Object controller = controllers.get(0);
        String controllerName = controller.getClass().getName();

        assertThat(controllerName).isEqualTo(TestController.class.getName());
    }
}
