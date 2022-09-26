package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

public class ControllerScannerTest {

    @DisplayName("@Controller 어노테이션이 붙은 클래스를 읽어와서 인스턴스를 생성한다.")
    @Test
    void getControllers() {
        // given
        final ControllerScanner controllerScanner = new ControllerScanner("samples");

        // when
        final Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        // then
        assertAll(
                () -> assertThat(controllers.size()).isEqualTo(1),
                () -> assertThat(controllers.keySet()).contains(TestController.class)
        );
    }
}
