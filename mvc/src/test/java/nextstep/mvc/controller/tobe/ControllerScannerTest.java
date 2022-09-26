package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import samples.TestController;

public class ControllerScannerTest {

    @Test
    @DisplayName("경로를 탐색해서 컨트롤러를 식별한다.")
    void getControllerMapping(){
        // given
        ControllerScanner controllerScanner = new ControllerScanner("samples");

        // when
        Map<Class<?>, Object> controllerMapping = controllerScanner.getControllerMapping();

        // then
        assertAll(
            () -> assertThat(controllerMapping.keySet()).hasSize(1),
            () -> assertThat(controllerMapping.keySet()).contains(TestController.class)
        );
    }
}
