package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

class ControllerScannerTest {

    @Test
    @DisplayName("@Controller가 붙은 모든 클래스를 찾는다.")
    void getControllers() {
        ControllerScanner controllerScanner = new ControllerScanner("samples");
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        assertThat(controllers.containsKey(TestController.class)).isTrue();
    }
}
