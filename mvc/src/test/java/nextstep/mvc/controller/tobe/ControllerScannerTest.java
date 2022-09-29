package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import samples.TestController;

class ControllerScannerTest {

    @Test
    void 컨트롤러_어노테이션이_붙은_클래스의_객체를_만들어서_반환한다() {
        // given
        final var controllerScanner = new ControllerScanner("samples");

        // when
        final List<Object> controllers = controllerScanner.scan();

        // then
        assertThat(controllers.get(0)).isInstanceOf(TestController.class);
    }
}
