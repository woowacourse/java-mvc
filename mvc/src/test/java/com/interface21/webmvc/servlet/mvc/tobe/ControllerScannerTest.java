package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ControllerScannerTest {

    @Test
    @DisplayName("탐색한 컨트롤러 클래스의 인스턴스를 생성한다")
    void 컨트롤러_인스턴스를_생성한다() {
        final Reflections reflections = mock(Reflections.class);
        when(reflections.getTypesAnnotatedWith(Controller.class))
                .thenReturn(Set.of(FirstController.class, SecondController.class));
        final ControllerScanner controllerScanner = new ControllerScanner(reflections);

        final var controllers = controllerScanner.getControllers();

        assertThat(controllers)
                .containsOnlyKeys(FirstController.class, SecondController.class);
        assertThat(controllers.get(FirstController.class))
                .isInstanceOf(FirstController.class);
        assertThat(controllers.get(SecondController.class))
                .isInstanceOf(SecondController.class);
    }

    @Test
    @DisplayName("기본 생성자가 없는 컨트롤러는 인스턴스 생성에 실패한다")
    void 기본_생성자가_없는_컨트롤러는_생성할_수_없다() {
        final Reflections reflections = mock(Reflections.class);
        when(reflections.getTypesAnnotatedWith(Controller.class))
                .thenReturn(Set.of(ControllerWithoutDefaultConstructor.class));
        final ControllerScanner controllerScanner = new ControllerScanner(reflections);

        assertThatThrownBy(controllerScanner::getControllers)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("컨트롤러 생성 실패")
                .hasCauseInstanceOf(NoSuchMethodException.class);
    }

    @Test
    @DisplayName("컨트롤러 생성자에서 예외가 발생하면 인스턴스 생성에 실패한다")
    void 생성자에서_예외가_발생한_컨트롤러는_생성할_수_없다() {
        final Reflections reflections = mock(Reflections.class);
        when(reflections.getTypesAnnotatedWith(Controller.class))
                .thenReturn(Set.of(ControllerWithFailingConstructor.class));
        final ControllerScanner controllerScanner = new ControllerScanner(reflections);

        assertThatThrownBy(controllerScanner::getControllers)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("컨트롤러 생성 실패")
                .hasCauseInstanceOf(InvocationTargetException.class);
    }

    @Controller
    public static class FirstController {
    }

    @Controller
    public static class SecondController {
    }

    @Controller
    public static class ControllerWithoutDefaultConstructor {

        public ControllerWithoutDefaultConstructor(final String value) {
        }
    }

    @Controller
    public static class ControllerWithFailingConstructor {

        public ControllerWithFailingConstructor() {
            throw new IllegalArgumentException("생성 실패");
        }
    }
}
