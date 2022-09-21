package nextstep.mvc.controller.tobe.mappings;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

class ControllerScannerTest {

    @Test
    @DisplayName("컨트롤러 어노테이션이 있는 클래스를 찾는다")
    void findControllers() {
        // given
        ControllerScanner scanner = new ControllerScanner();
        // when
        Set<Class<?>> classes = scanner.findClasses("samples");
        // then
        assertThat(classes).containsOnly(TestController.class);
    }
}
