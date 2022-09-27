package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import nextstep.mvc.controller.ControllerScanner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ControllerScannerTest {

    @DisplayName("패키지 내의 @Controller가 붙은 객체들을 스캔한다.")
    @Test
    void scan() {
        final ControllerScanner controllerScanner = new ControllerScanner(new Object[]{"samples"});
        final Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        assertThat(controllers.size()).isEqualTo(1);
    }
}
