package com.interface21.webmvc.servlet.mvc.tobe;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ControllerScannerTest {

    @Test
    @DisplayName("지정한 패키지에서 @Controller 클래스를 찾는다")
    void findsControllersInBasePackage() {
        final var scanner = new ControllerScanner("samples");

        assertThat(scanner.getControllers()).containsExactly(TestController.class);
    }

    @Test
    @DisplayName("컨트롤러 클래스를 인스턴스로 만들고 클래스와 연결한다")
    void instantiatesControllersByClass() {
        final var scanner = new ControllerScanner("samples");

        final var controllers = scanner.instantiateControllers(Set.of(TestController.class));

        assertThat(controllers).containsOnlyKeys(TestController.class);
        assertThat(controllers.get(TestController.class)).isInstanceOf(TestController.class);
    }
}
