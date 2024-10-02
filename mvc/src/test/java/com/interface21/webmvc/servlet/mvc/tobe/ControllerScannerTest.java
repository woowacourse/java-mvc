package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.mvc.ControllerScanner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import samples.TestController;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ControllerScannerTest {

    @Test
    @DisplayName("주입된 리플렉션에서 @Controller 어노테이션이 붙은 클래스를 찾는다.")
    void test() {
        ControllerScanner controllerScanner = new ControllerScanner(new Reflections("samples"));

        Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        assertThat(controllers.keySet()).containsExactly(TestController.class);
    }
}
