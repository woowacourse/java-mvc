package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.interface21.context.stereotype.Controller;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("컨트롤러 스캐너 테스트")
class ControllerScannerTest {

    private ControllerScanner controllerScanner;

    @BeforeEach
    void setUp() {
        controllerScanner = new ControllerScanner("com.interface21.webmvc");
    }

    @DisplayName("객체 생성 시 파라미터로 전달된 패키지 내의 컨트롤러 클래스를 스캔할 수 있다")
    @Test
    void testScanControllers() {
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        Assertions.assertAll(
                () -> assertThat(controllers).isNotEmpty(),
                () -> assertThat(controllers).containsKey(SampleController.class),
                () -> assertThat(controllers.get(SampleController.class)).isInstanceOf(SampleController.class)
        );
    }

    @DisplayName("컨트롤러스캐너에서 반환한 컨트롤러 정보를 변경하려고 한다면 예외가 발생한다")
    @Test
    void controllersReturnedAsUnmodifiableMap() {
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        assertThatThrownBy(() -> controllers.put(Object.class, new Object()))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Controller
    private static class SampleController {

        public SampleController() {
        }
    }
}
