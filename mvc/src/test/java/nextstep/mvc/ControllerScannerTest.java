package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

class ControllerScannerTest {

    @DisplayName("Controller를 불러오는지 확인한다.")
    @Test
    void getControllers() {
        // given
        final ControllerScanner controllerScanner = new ControllerScanner("samples");

        // when
        final Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        // then
        assertAll(
                () -> assertThat(controllers).hasSize(1),
                () -> assertThat(controllers).containsKey(TestController.class)
        );
    }
}
