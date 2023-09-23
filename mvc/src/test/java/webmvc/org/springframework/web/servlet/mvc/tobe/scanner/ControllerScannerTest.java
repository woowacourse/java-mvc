package webmvc.org.springframework.web.servlet.mvc.tobe.scanner;

import org.junit.jupiter.api.Test;
import samples.TestController;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ControllerScannerTest {

    @Test
    void 컨트롤러_매핑_정보를_리플랙션으로_가져온다() {
        // given
        final Map<Class<?>, Object> controller = new ControllerScanner().getControllers("samples");

        // expect
        assertThat(controller)
                .usingRecursiveComparison()
                .isEqualTo(Map.of(TestController.class, new TestController()));
    }
}
