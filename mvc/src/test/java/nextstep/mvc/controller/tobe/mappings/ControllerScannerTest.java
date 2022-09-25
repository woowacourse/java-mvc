package nextstep.mvc.controller.tobe.mappings;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

class ControllerScannerTest {

    @Test
    @DisplayName("컨트롤러 어노테이션이 있는 클래스로 인스턴스를 만든다")
    void findControllers() {
        // given
        ControllerScanner scanner = new ControllerScanner();
        // when
        Map<Class<?>, Object> instanceByClasses = scanner.createInstanceByClasses("samples");
        // then
        assertThat(instanceByClasses.keySet()).containsOnly(TestController.class);
    }
}
