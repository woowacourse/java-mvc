package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.Test;

public class ControllerScannerTest {

    @Test
    void findAllControllers() {
        Map<Class<?>, Object> allControllers = ControllerScanner.findAllControllers("samples");

        assertThat(allControllers.size()).isEqualTo(1);
    }
}
