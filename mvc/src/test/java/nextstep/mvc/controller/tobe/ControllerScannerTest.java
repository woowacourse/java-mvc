package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.mvc.controller.ControllerScanner;
import samples.TestController;

class ControllerScannerTest {

    @DisplayName("Controller을 정상적으로 가져오는지를 테스트한다.")
    @Test
    void getController() {
        final ControllerScanner controllerScanner = new ControllerScanner("samples");
        final Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        assertThat(controllers.get(TestController.class)).isNotNull();
    }
}
