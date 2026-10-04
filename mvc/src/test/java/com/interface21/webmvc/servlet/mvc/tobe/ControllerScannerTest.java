package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

public class ControllerScannerTest {

    @Test
    @DisplayName("컨트롤러를 찾아 인스턴스를 생성한다.")
    void testControllerScanner() {
        // given
        ControllerScanner scanner = new ControllerScanner("samples");

        // when
        Map<Class<?>, Object> findController = scanner.getControllers();

        // when
        assertThat(findController).containsOnlyKeys(TestController.class);
        assertThat(findController.get(TestController.class)).isInstanceOf(TestController.class);
    }
}
