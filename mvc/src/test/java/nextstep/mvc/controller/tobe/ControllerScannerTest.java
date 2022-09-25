package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

class ControllerScannerTest {

    @Test
    @DisplayName("패키지 내에 있는 controller 클래스를 스캔한다.")
    void scanController() {
        final ControllerScanner controllerScanner = new ControllerScanner("samples");

        final Map<Class<?>, Object> actual = controllerScanner.scanController();

        assertThat(actual).containsKey(TestController.class);
    }
}
