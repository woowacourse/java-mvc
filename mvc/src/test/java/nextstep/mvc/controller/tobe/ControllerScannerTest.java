package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ControllerScannerTest {

    @Test
    @DisplayName("sample 패키지에 Controller 어노테이션이 붙은 클래스를 찾을 수 있다.")
    void findAllControllers() {
        ControllerScanner controllerScanner = ControllerScanner.from("samples");

        Set<Class<?>> controllers = controllerScanner.findAllControllers();

        assertThat(controllers).hasSize(1);
    }
}
