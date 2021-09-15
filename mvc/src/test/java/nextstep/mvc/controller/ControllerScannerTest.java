package nextstep.mvc.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ControllerScannerTest {

    @DisplayName("Controller를 Set으로 가져온다. - 성공")
    @Test
    void getControllers() {
        // given
        String basePackage = "samples";

        // when
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);

        // then
        final Set<Object> controllers = controllerScanner.getControllers();
        assertThat(controllers).hasSize(1);
    }
}
