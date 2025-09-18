package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.Test;

class ControllerScannerTest {

    @Test
    void Controller_어노테이션이_붙은_클래스를_가져올_수_있다() {
        ControllerScanner scanner = new ControllerScanner();

        Map<Class<?>, Object> controllers = scanner.getControllers("samples");

        assertThat(controllers.size()).isEqualTo(1);
    }
}
