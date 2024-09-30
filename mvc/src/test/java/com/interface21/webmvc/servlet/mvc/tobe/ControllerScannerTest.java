package com.interface21.webmvc.servlet.mvc.tobe;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import samples.TestController;
import java.util.Map;

class ControllerScannerTest {

    @DisplayName("패키지 이름에 따른 컨트롤러를 반환한다.")
    @Test
    void getControllers() {
        // given
        Object[] basePackage = new Object[1];
        basePackage[0] = TestController.class.getPackage().getName();

        Reflections reflections = new Reflections(basePackage);
        ControllerScanner controllerScanner = new ControllerScanner(reflections);

        // when
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        // then
        Assertions.assertThat(controllers).hasSize(1);
    }
}
