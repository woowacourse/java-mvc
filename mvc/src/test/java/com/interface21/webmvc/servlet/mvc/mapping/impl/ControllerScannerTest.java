package com.interface21.webmvc.servlet.mvc.mapping.impl;

import static org.assertj.core.api.Assertions.assertThat;

import com.interface21.context.stereotype.Controller;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ControllerScannerTest {

    @Test
    @DisplayName("특정 패키지에 있는 컨트롤러 어노테이션이 붙은 컨트롤러를 스캔한다")
    void scanPackage() {
        // given
        String packageName = TestController.class.getPackage().getName();

        // when
        ControllerScanner controllerScanner = new ControllerScanner(packageName);
        Map<Class<?>, Object> controllerInstance = controllerScanner.getControllerInstance();

        // then
        assertThat(controllerInstance).containsKeys(TestController.class);
    }

    @Controller
    static class TestController {
    }
}
