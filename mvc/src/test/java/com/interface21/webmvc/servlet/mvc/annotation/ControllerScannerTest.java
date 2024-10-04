package com.interface21.webmvc.servlet.mvc.annotation;

import static org.assertj.core.api.Assertions.assertThat;

import com.interface21.context.stereotype.Controller;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

class ControllerScannerTest {

    public static final String SAMPLES_TEST_CONTROLLER = "samples";

    private final ControllerScanner controllerScanner = new ControllerScanner(new Object[]{SAMPLES_TEST_CONTROLLER});

    @Test
    @DisplayName("주어진 패키지 하위의 모든 @Controller 어노테이션이 붙은 클래스를 탐색한다.")
    void getControllers() {
        final Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        Object instance = controllers.get(TestController.class);

        assertThat(instance.getClass().isAnnotationPresent(Controller.class)).isTrue();
    }
}
