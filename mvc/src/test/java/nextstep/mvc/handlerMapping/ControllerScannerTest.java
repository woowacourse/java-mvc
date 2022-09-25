package nextstep.mvc.handlerMapping;

import static org.assertj.core.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.Test;

import samples.TestController;

class ControllerScannerTest {

    @Test
    void getControllers() {
        Object[] basePackage = {"samples"};
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);

        Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        assertThat(controllers.get(TestController.class))
            .isInstanceOf(TestController.class);
    }

}
