package com.interface21.webmvc.servlet.mvc.tobe;

import org.junit.jupiter.api.Test;
import samples.MappingTestController;
import samples.TestController;

import static org.assertj.core.api.Assertions.assertThat;

class ControllerScannerTest {

    @Test
    void 지정한_패키지의_컨트롤러들을_찾아_인스턴스를_생성한다() {
        final var controllers = new ControllerScanner("samples").getControllers();

        assertThat(controllers).containsOnlyKeys(TestController.class, MappingTestController.class);
        assertThat(controllers.get(TestController.class)).isInstanceOf(TestController.class);
        assertThat(controllers.get(MappingTestController.class)).isInstanceOf(MappingTestController.class);
    }
}
