package com.interface21.webmvc.servlet.mvc.tobe;

import org.junit.jupiter.api.Test;
import samples.TestController;

import static org.assertj.core.api.Assertions.assertThat;

class ControllerScannerTest {

    @Test
    void Controller가_붙은_클래스를_찾아_객체를_생성한다() {
        final var controllerScanner = new ControllerScanner("samples");

        final var controllers = controllerScanner.getControllers();

        assertThat(controllers).containsOnlyKeys(TestController.class);
        assertThat(controllers.get(TestController.class)).isInstanceOf(TestController.class);
    }

    @Test
    void Controller가_없는_클래스는_생성하지_않는다() {
        final var controllerScanner = new ControllerScanner("com.interface21.webmvc.servlet");

        final var controllers = controllerScanner.getControllers();

        assertThat(controllers).isEmpty();
    }
}
