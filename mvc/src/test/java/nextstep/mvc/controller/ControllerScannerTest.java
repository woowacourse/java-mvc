package nextstep.mvc.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import samples.TestAnnotationController;

class ControllerScannerTest {

    @DisplayName("지정한 경로 내의 @Controller 가 있는 모든 객체를 찾는다")
    @Test
    void getControllers() {
        final Map<Class<?>, Object> controllers = ControllerScanner.getControllers("samples");

        assertAll(
            () -> assertThat(controllers).containsOnlyKeys(TestAnnotationController.class),
            () -> assertThat(controllers.get(TestAnnotationController.class)).isInstanceOf(TestAnnotationController.class)
        );
    }
}
