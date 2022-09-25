package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import samples.TestController;

class ControllerScannerTest {

    @Test
    @DisplayName("@Controller 어노테이션이 붙은 클래스들을 인스턴스화 하여 반환한다.")
    void getControllers() {
        // given
        final ControllerScanner controllerScanner = new ControllerScanner(new String[] {"samples"});

        // when
        final Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        final List<Object> instances = new ArrayList<>(controllers.values());
        final Object testController = instances.get(0);

        // then
        assertAll(
            () -> assertThat(testController).isInstanceOf(TestController.class),
            () -> assertThat(instances).hasSize(1)
        );
    }

}
