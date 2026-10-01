package com.interface21.webmvc.servlet.mvc.tobe;

import org.junit.jupiter.api.Test;
import samples.NotController;
import samples.TestController;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ControllerScannerTest {

    @Test
    void Controller_어노테이션이_붙은_클래스를_찾는다() {
        final ControllerScanner controllerScanner = new ControllerScanner("samples");

        final Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        assertThat(controllers).containsKey(TestController.class);
    }

    @Test
    void 찾은_클래스의_인스턴스를_생성한다() {
        final ControllerScanner controllerScanner = new ControllerScanner("samples");

        final Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        assertThat(controllers.get(TestController.class)).isInstanceOf(TestController.class);
    }

    @Test
    void Controller_어노테이션이_없는_클래스는_찾지_않는다() {
        final ControllerScanner controllerScanner = new ControllerScanner("samples");

        final Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        assertThat(controllers).doesNotContainKey(NotController.class);
    }

    @Test
    void 기본_생성자가_없는_컨트롤러면_어떤_컨트롤러인지_알려주는_예외가_발생한다() {
        assertThatThrownBy(() -> new ControllerScanner("invalidsamples.noconstructor").getControllers())
                .hasMessageContaining("NoDefaultConstructorController");
    }
}
