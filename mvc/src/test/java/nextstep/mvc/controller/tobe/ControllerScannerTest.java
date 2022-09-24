package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ControllerScannerTest {

    @Test
    void basePacakage를_스캔하여_어노테이션이_붙은_컨트롤러를_찾아_인스턴스를_생성한다() {
        ControllerScanner controllerScanner = new ControllerScanner(getClass().getPackageName());

        assertThat(controllerScanner.getControllers()).containsOnlyKeys(DummyController.class);
    }
}
