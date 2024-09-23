package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

class ControllerScannerTest {

    @DisplayName("@Controller 어노테이션이 붙은 클래스를 인스턴스화 할 수 없는 경우 예외가 발생한다.")
    @Test
    void throwsWhenCannotInstantiateController() {
        // given
        Reflections reflections = new Reflections("com.interface21.webmvc.servlet.mvc.tobe");
        ControllerScanner controllerScanner = new ControllerScanner(reflections);

        // when & then
        assertThatThrownBy(controllerScanner::getControllers)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("컨트롤러를 인스턴스화 할 수 없습니다.");
    }
}
