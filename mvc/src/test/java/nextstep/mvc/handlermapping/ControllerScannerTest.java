package nextstep.mvc.handlermapping;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import samples.TestController;
import samples.TestController2;

class ControllerScannerTest {

    @Test
    void Controller_어노테이션이_붙은_클래스를_모두_찾아올_수_있다() {
        // given
        final ControllerScanner controllerScanner = new ControllerScanner("samples");

        // when
        List<Object> controllers = controllerScanner.getControllers();

        // then
        assertThat(controllers).hasExactlyElementsOfTypes(TestController.class, TestController2.class);
    }
}
