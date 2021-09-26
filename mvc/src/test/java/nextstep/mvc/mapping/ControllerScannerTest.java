package nextstep.mvc.mapping;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ControllerScannerTest {

    @DisplayName("패키지에서 컨트롤러를 스캔한 뒤 반환한다")
    @Test
    void scanControllers() {
        // given
        ControllerScanner controllerScanner = new ControllerScanner("samples");

        // when
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        Object instance = controllers.get(TestController.class);

        // then
        assertThat(controllers).containsKey(TestController.class);
        assertThat(instance).isInstanceOf(TestController.class);
    }
}
