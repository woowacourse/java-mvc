package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.Test;
import samples.TestController;

class ControllerScannerTest {

    @Test
    void controller_클래스와_인스턴스로_구성된_맵을_얻을_수_있다() {
        // given
        final ControllerScanner scanner = new ControllerScanner("samples");

        // when
        final Map<Class<?>, Object> controllers = scanner.getControllers();

        // then
        assertThat(controllers).containsKey(TestController.class);
    }
}
