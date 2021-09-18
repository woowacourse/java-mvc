package nextstep.mvc.controller.tobe;

import nextstep.mvc.controller.ControllerScanner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ControllerScannerTest {

    @DisplayName("패키지에서 컨트롤러를 찾는다.")
    @Test
    void scanControllers() {
        ControllerScanner controllerScanner = new ControllerScanner("samples");
        Map<Class<?>, Object> controllers = controllerScanner.scanControllers();
        assertThat(controllers).containsKey(TestController.class);
    }
}
