package nextstep.mvc.controller.tobe;

import org.junit.jupiter.api.Test;
import samples.TestController;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ControllerScannerTest {

    @Test
    void 주어진_패키지_내의_컨트롤러_어노테이션이_적용된_클래스를_스캔한다() {
        // given
        final ControllerScanner controllerScanner = new ControllerScanner("samples");
        // when
        final Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        // then
        assertAll(
                () -> assertThat(controllers).hasSize(1),
                () -> assertThat(controllers).containsKey(TestController.class),
                () -> assertThat(controllers.get(TestController.class)).usingRecursiveComparison()
                        .isEqualTo(new TestController())
        );
    }
}
