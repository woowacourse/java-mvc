package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import samples.TestController;

class ControllerScannerTest {

    @Test
    @DisplayName("Reflections를 이용해 특정 패키지 하위 클래스 중 @Controller 애너테이션이 붙어있는 모든 클래스 정보를 등록한다")
    void getControllers() {
        Reflections reflections = new Reflections("samples");
        ControllerScanner controllerScanner = new ControllerScanner(reflections);
        controllerScanner.initialize();

        Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        assertThat(controllers.size()).isEqualTo(1);
        assertThat(controllers.get(TestController.class)).isInstanceOf(TestController.class);
    }
}
