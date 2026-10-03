package com.interface21.webmvc.servlet.mvc.tobe;

import invalidsamples.NoDefaultConstructorController;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import samples.PrefixController;
import samples.TestController;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ControllerScannerTest {

    @Test
    void Controller_애노테이션이_붙은_클래스를_찾아_인스턴스를_생성한다() {
        final var controllerScanner = new ControllerScanner(new Reflections("samples"));

        final var controllers = controllerScanner.getControllers();

        assertThat(controllers).containsOnlyKeys(TestController.class, PrefixController.class);
        assertThat(controllers.get(TestController.class)).isInstanceOf(TestController.class);
        assertThat(controllers.get(PrefixController.class)).isInstanceOf(PrefixController.class);
    }

    @Test
    void 기본_생성자가_없는_컨트롤러는_생성에_실패한다() {
        final var controllerScanner = new ControllerScanner(new Reflections("invalidsamples"));

        assertThatThrownBy(controllerScanner::getControllers)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("컨트롤러를 생성할 수 없습니다")
                .hasMessageContaining(NoDefaultConstructorController.class.getName());
    }
}
