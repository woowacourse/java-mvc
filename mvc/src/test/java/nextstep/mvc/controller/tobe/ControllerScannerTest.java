package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestAnnotatedController;

class ControllerScannerTest {

    @DisplayName("특정 패키지 내에 Controller Annotation이 달린 class를 찾는다.")
    @Test
    void getControllers() {
        final ControllerScanner controllerScanner = new ControllerScanner(new Object[]{"samples"});
        final Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        assertThat(controllers.keySet()).containsExactly(TestAnnotatedController.class);
    }
}
