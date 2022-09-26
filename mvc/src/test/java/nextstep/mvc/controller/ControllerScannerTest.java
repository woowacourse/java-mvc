package nextstep.mvc.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import nextstep.mvc.controller.ControllerScanner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

public class ControllerScannerTest {

    @DisplayName("@Controller가 붙은 클래스를 찾을 수 있다.")
    @Test
    void findAnnotationController() {
        // given
        ControllerScanner controllerScanner = new ControllerScanner("samples");

        // when
        Set<Class<?>> controllers = controllerScanner.getControllers();

        // then
        assertThat(controllers).contains(TestController.class);
    }
}
