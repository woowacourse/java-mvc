package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

class ControllerScannerTest {

    @DisplayName("reflection을 활용하여 Controller 어노테이션이 붙은 클래스를 찾아 반환한다.")
    @Test
    void getControllers() {
        ControllerScanner scanner = ControllerScanner.from("samples");
        Map<Class<?>, Object> controllers = scanner.getControllers();

        assertThat(controllers).containsOnlyKeys(TestController.class);
    }
}
