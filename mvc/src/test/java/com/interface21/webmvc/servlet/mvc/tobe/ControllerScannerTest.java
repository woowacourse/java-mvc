package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.interface21.context.stereotype.Controller;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ControllerScannerTest {

    @DisplayName("생성자로 받은 패키지의 컨트롤러를 찾아 인스턴스와 함께 반환한다.")
    @Test
    void test() {
        // Given
        String basePackage = this.getClass().getPackage().getName();
        ControllerScanner scanner = new ControllerScanner(basePackage);

        // When
        Map<Class<?>, Object> controllers = scanner.getControllers();

        // Then
        assertAll(
                () -> assertThat(controllers.containsKey(TestController1.class))
                        .isTrue(),
                () -> assertThat(controllers.containsKey(TestController2.class))
                        .isTrue(),
                () -> assertThat(controllers.containsKey(NonControllerClass.class))
                        .isFalse(),
                () -> assertThat(controllers.get(TestController1.class) instanceof TestController1)
                        .isTrue(),
                () -> assertThat(controllers.get(TestController2.class) instanceof TestController2)
                        .isTrue()
        );
    }

    @Controller
    static class TestController1 {
    }

    @Controller
    static class TestController2 {
    }

    static class NonControllerClass {
    }
}
