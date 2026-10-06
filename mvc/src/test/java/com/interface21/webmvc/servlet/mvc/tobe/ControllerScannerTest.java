package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.Test;
import samples.TestController;

public class ControllerScannerTest {

    @Test
    void Controller_애너테이션이_붙은_클래스를_찾아_인스턴스를_만든다() {
        final ControllerScanner scanner = new ControllerScanner("samples");

        final Map<Class<?>, Object> controllers = scanner.getControllers();

        assertThat(controllers).containsKey(TestController.class);
        assertThat(controllers.get(TestController.class)).isInstanceOf(TestController.class);
    }

}
