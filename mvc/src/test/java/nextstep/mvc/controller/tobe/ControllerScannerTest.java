package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ControllerScannerTest {

    @Test
    @DisplayName("컨트롤러를 정상적으로 찾는다.")
    void initControllerScanner() {
        final ControllerScanner sut = new ControllerScanner("samples");

        assertThat(sut.getControllers()).hasSize(1);
    }
}
