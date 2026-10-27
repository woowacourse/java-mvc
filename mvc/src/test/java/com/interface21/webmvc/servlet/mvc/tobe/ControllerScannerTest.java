package com.interface21.webmvc.servlet.mvc.tobe;

import org.junit.jupiter.api.Test;
import samples.MappingTestController;
import samples.TestController;
import scannerfixtures.NoDefaultConstructorController;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ControllerScannerTest {

    @Test
    void 지정한_패키지의_컨트롤러들을_찾아_인스턴스를_생성한다() {
        final var controllers = new ControllerScanner("samples").getControllers();

        assertThat(controllers).containsOnlyKeys(TestController.class, MappingTestController.class);
        assertThat(controllers.get(TestController.class)).isInstanceOf(TestController.class);
        assertThat(controllers.get(MappingTestController.class)).isInstanceOf(MappingTestController.class);
    }

    @Test
    void 기본_생성자가_없는_컨트롤러를_만나면_컨트롤러_이름과_원인을_담은_IllegalStateException을_던진다() {
        final var scanner = new ControllerScanner("scannerfixtures");

        assertThatThrownBy(scanner::getControllers)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(NoDefaultConstructorController.class.getName())
                .hasCauseInstanceOf(NoSuchMethodException.class);
    }
}
