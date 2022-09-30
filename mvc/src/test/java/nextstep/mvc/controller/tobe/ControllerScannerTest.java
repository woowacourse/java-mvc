package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import samples.TestController;

class ControllerScannerTest {

    @DisplayName("입력 패키지에 있는 @Controller가 있는 클래스 인스턴스로 저장한다.")
    @Test
    void getController() {
        Object[] basePackage = {"samples"};
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);

        Map<Class<?>, Object> controllers = controllerScanner.getController();
        Object actual = controllers.get(TestController.class);

        assertThat(actual.getClass()).isEqualTo(TestController.class);
    }
}
