package com.interface21.webmvc.servlet.mvc.tobe;

import org.junit.jupiter.api.Test;
import samples.AllMethodTestController;
import samples.TestController;

import static org.assertj.core.api.Assertions.assertThat;

class ControllerScannerTest {

    @Test
    void Controller_어노테이션이_붙은_클래스를_찾아_인스턴스를_생성한다() {
        final var controllerScanner = new ControllerScanner("samples");

        final var controllers = controllerScanner.getControllers();

        assertThat(controllers).containsOnlyKeys(TestController.class, AllMethodTestController.class);
        assertThat(controllers.get(TestController.class)).isInstanceOf(TestController.class);
        assertThat(controllers.get(AllMethodTestController.class)).isInstanceOf(AllMethodTestController.class);
    }
}
