package com.interface21.webmvc.servlet.mvc.tobe;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("컨트롤러 탐색")
class ControllerScannerTest {

    @Test
    @DisplayName("지정한 패키지에서 @Controller가 붙은 클래스만 찾는다")
    void findsOnlyAnnotatedControllers() {
        // given
        final var scanner = new ControllerScanner("samples");

        // when
        final var controllers = scanner.getControllers();

        // then
        assertThat(controllers.keySet()).containsExactly(TestController.class);
    }

    @Test
    @DisplayName("찾은 컨트롤러 클래스의 객체를 생성한다")
    void instantiatesController() {
        // given
        final var scanner = new ControllerScanner("samples");

        // when
        final var controllers = scanner.getControllers();

        // then
        assertThat(controllers.get(TestController.class)).isInstanceOf(TestController.class);
    }
}
