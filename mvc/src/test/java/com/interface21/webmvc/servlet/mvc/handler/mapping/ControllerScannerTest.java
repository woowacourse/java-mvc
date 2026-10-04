package com.interface21.webmvc.servlet.mvc.handler.mapping;

import org.junit.jupiter.api.Test;
import samples.TestController;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ControllerScannerTest {

    @Test
    void Controller_어노테이션이_설정된_클래스를_찾아_인스턴스를_생성한다() {
        final var controllerScanner = new ControllerScanner("samples");

        final Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        assertThat(controllers).containsOnlyKeys(TestController.class);
        assertThat(controllers.get(TestController.class)).isInstanceOf(TestController.class);
    }

    @Test
    void Controller_어노테이션이_설정된_클래스가_없으면_빈_Map을_반환한다() {
        final var controllerScanner = new ControllerScanner("com.interface21.webmvc.servlet.view");

        assertThat(controllerScanner.getControllers()).isEmpty();
    }
}
