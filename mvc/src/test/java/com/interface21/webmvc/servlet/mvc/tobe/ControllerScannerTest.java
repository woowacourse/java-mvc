package com.interface21.webmvc.servlet.mvc.tobe;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.MappingVariantsController;
import samples.TestController;

import static org.assertj.core.api.Assertions.assertThat;

class ControllerScannerTest {

    @Test
    @DisplayName("지정한 패키지의 어노테이션 컨트롤러를 찾아 인스턴스를 생성한다")
    void scansAnnotatedControllers() {
        // given
        final var scanner = new ControllerScanner("samples");

        // when
        final var controllers = scanner.scan();

        // then
        assertThat(controllers.get(TestController.class)).isInstanceOf(TestController.class);
        assertThat(controllers.get(MappingVariantsController.class)).isInstanceOf(MappingVariantsController.class);
    }
}
