package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import samples.PrefixController;
import samples.TestController;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
        final var reflections = mock(Reflections.class);
        when(reflections.getTypesAnnotatedWith(Controller.class)).thenReturn(Set.of(NoDefaultConstructorController.class));
        final var controllerScanner = new ControllerScanner(reflections);

        assertThatThrownBy(controllerScanner::getControllers)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("컨트롤러를 생성할 수 없습니다")
                .hasMessageContaining(NoDefaultConstructorController.class.getName());
    }

    static class NoDefaultConstructorController {

        private final String name;

        NoDefaultConstructorController(final String name) {
            this.name = name;
        }
    }
}
