package nextstep.mvc.controller.scanner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

class ControllerScannerTest {

    @DisplayName("@Controller 애노테이션이 붙은 객체를 스캔한다.")
    @Test
    void scanController() {
        // given
        String basePackage = "samples";

        // then
        Set<Class<?>> controllers = ControllerScanner.scanController(basePackage);

        // then
        assertThat(controllers).contains(TestController.class);
    }
}
