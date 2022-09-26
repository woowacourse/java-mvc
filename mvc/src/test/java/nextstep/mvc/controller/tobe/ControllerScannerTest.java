package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

class ControllerScannerTest {

    @Test
    @DisplayName("컨트롤러 어노테이션이 있는 클래스를 찾는다.")
    void findControllers() throws Exception {
        final ControllerScanner controllerScanner = ControllerScanner.from(new Object[]{"samples"});

        final Map<Class<?>, Object> controllers =controllerScanner.findControllers();

        assertThat(controllers.keySet()).containsExactly(TestController.class);
    }
}
