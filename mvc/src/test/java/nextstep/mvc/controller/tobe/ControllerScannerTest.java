package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ControllerScannerTest {

    @DisplayName("Contoller 애노테이션을 가진 객체의 Class파일들을 전부 읽어온다.")
    @Test
    void Controller_애노테이션을_가진_객체의_Class파일들을_전부_읽어온다() {
        ControllerScanner controllerScanner = new ControllerScanner();

        Object[] basePackage = {"samples"};
        Map<Class<?>, Object> controllers = controllerScanner.getControllers(basePackage);

        assertThat(controllers).hasSize(1);
    }
}
