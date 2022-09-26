package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

class ControllerScannerTest {

    @Test
    @DisplayName("지정한 패키지 하위의 컨트롤러들을 찾아낸다..")
    void test() {
        // given
        ControllerScanner controllerScanner = new ControllerScanner(new Reflections("samples"));

        // when
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        // then
        assertThat(controllers.size()).isEqualTo(1);
    }
}
