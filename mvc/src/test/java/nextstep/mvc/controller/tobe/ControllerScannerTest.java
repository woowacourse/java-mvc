package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

public class ControllerScannerTest {

    @Test
    @DisplayName("컨트롤러를 정상적으로 찾는다.")
    void initControllerScanner() {
        final ControllerScanner sut = new ControllerScanner("samples");

        assertAll(
                () -> assertThat(sut.getControllers()).hasSize(1),
                () -> assertThat(sut.getControllers()).contains(TestController.class)
        );
    }
}
