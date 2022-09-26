package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import org.junit.jupiter.api.Test;
import samples.TestController;

class ControllerScannerTest {

    @Test
    void controller_scanner를_생성하면_controllers를_등록한다() {
        // given
        Object[] basePackages = new Object[] {"samples"};

        // when
        ControllerScanner controllerScanner = new ControllerScanner(basePackages);
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        // then
        assertThat(controllers.get(TestController.class)).isNotNull();
    }

}
