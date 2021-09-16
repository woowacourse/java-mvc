package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import com.techcourse.controller.RestController;
import com.techcourse.controller.UserController;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ControllerScannerTest {

    @Test
    void getControllers() {
        ControllerScanner controllerScanner = new ControllerScanner("com.techcourse.controller");
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        assertThat(controllers.keySet()).hasSize(2);
        assertThat(controllers.containsKey(RestController.class)).isTrue();
        assertThat(controllers.containsKey(UserController.class)).isTrue();
    }
}
