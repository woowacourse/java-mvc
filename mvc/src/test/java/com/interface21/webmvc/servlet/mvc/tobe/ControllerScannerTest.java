package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import scanner.samples.ScannedController;

class ControllerScannerTest {

    @Test
    @DisplayName("@Controller가 붙은 클래스만 찾아 인스턴스를 생성한다")
    void scan_controllers() throws Exception {
        final var scanner = new ControllerScanner("scanner.samples");

        final var controllers = scanner.scan();

        assertThat(controllers).containsOnlyKeys(ScannedController.class);
        final var controller = (ScannedController) controllers.get(ScannedController.class);
        assertThat(controller.message()).isEqualTo("scanned");
    }

    @Test
    @DisplayName("중복된 패키지에서 발견한 컨트롤러는 한 번만 생성한다")
    void create_each_controller_once() throws Exception {
        final var scanner = new ControllerScanner("scanner", "scanner.samples");
        final var before = ScannedController.constructionCount;

        final var controllers = scanner.scan();

        assertThat(controllers).hasSize(1);
        assertThat(ScannedController.constructionCount - before).isEqualTo(1);
    }
}
