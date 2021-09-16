package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.Test;
import samples.TestController;
import samples.TestController2;
import samples.TestController3;
import samples.TestController4;
import samples.TestController5;

class ControllerScannerTest {

    @Test
    void getControllers() {
        final Object[] basePackage = {"samples"};
        final ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        final Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        Class<?>[] expected = {TestController.class, TestController2.class, TestController3.class,
                TestController4.class, TestController5.class};
        assertThat(controllers.keySet()).containsExactlyInAnyOrder(expected);
    }
}
