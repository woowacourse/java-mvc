package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

class ControllerScannerTest {

    @Test
    @DisplayName("Controller 애너테이션 기반의 클래스들을 반환한다.")
    void getControllers() {
        // given
        final String[] basePackages = {"samples"};
        final ControllerScanner controllerScanner = new ControllerScanner(basePackages);

        // when
        final Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        // then
        assertThat(controllers).containsKey(TestController.class);
    }
}
