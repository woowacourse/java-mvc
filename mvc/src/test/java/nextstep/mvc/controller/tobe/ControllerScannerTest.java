package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.Test;
import samples.TestController;

class ControllerScannerTest {
    @Test
    void controllerScan() {
        ControllerScanner scanner = new ControllerScanner();
        Map<Class<?>, Object> controllers = scanner.getControllers(new String[]{"samples"});

        Class<?> clazz = TestController.class;

        assertThat(controllers.values()).isNotEmpty();
        assertThat(controllers.get(clazz)).isInstanceOf(clazz);
    }
}
