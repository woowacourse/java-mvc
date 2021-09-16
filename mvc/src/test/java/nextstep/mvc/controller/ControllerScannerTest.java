package nextstep.mvc.controller;

import nextstep.web.annotation.Controller;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ControllerScanner 테스트")
class ControllerScannerTest {

    @DisplayName("@Controller가 달려있는 클래스 스캔")
    @Test
    void scanController() {
        // given
        final ControllerScanner controllerScanner = new ControllerScanner("samples");

        // when
        controllerScanner.initialize();
        final List<Object> controllers = controllerScanner.getControllers();

        // then
        assertThat(controllers).hasSize(1);
        final Object controller = controllers.get(0);
        assertThat(controller).isExactlyInstanceOf(TestController.class);
        assertThat(controller.getClass().isAnnotationPresent(Controller.class)).isTrue();
    }
}