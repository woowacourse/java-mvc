package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

class ControllerScannerTest {

    @DisplayName("controller 클래스와 객체를 반환한다.")
    @Test
    void getControllers()
            throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        ControllerScanner controllerScanner = ControllerScanner.from("samples");
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        assertThat(controllers.keySet()).contains(TestController.class);
    }
}
