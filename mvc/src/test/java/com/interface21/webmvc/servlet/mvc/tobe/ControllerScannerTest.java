package com.interface21.webmvc.servlet.mvc.tobe;

import org.junit.jupiter.api.Test;
import samples.TestController;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ControllerScannerTest {

    @Test
    void Controller_애노테이션이_붙은_클래스를_찾아_인스턴스를_생성한다() {
        final var controllerScanner = new ControllerScanner("samples");

        final var controllers = controllerScanner.getControllers();

        assertThat(controllers).containsOnlyKeys(TestController.class);
        assertThat(controllers.get(TestController.class)).isInstanceOf(TestController.class);
    }

    @Test
    void 컨트롤러가_없는_패키지를_스캔하면_빈_결과를_반환한다() {
        final var controllerScanner = new ControllerScanner("com.interface21.webmvc.servlet.view");

        assertThat(controllerScanner.getControllers()).isEmpty();
    }

    @Test
    void public_기본_생성자가_없는_컨트롤러는_인스턴스를_생성할_수_없다() {
        final var controllerScanner = new ControllerScanner("nodefaultconstructor");

        assertThatThrownBy(controllerScanner::getControllers)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("NoDefaultConstructorController");
    }
}
