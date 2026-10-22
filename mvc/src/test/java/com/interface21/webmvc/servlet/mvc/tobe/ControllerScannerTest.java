package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import invalidcontrollers.missing.MissingDefaultConstructorController;
import invalidcontrollers.throwing.ThrowingController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import scanner.samples.ScannedController;

import java.lang.reflect.InvocationTargetException;

class ControllerScannerTest {

    @Test
    @DisplayName("@Controller가 붙은 클래스만 찾아 인스턴스를 생성한다")
    void scan_controllers() {
        final var scanner = new ControllerScanner("scanner.samples");

        final var controllers = scanner.scan();

        assertThat(controllers).containsOnlyKeys(ScannedController.class);
        final var controller = (ScannedController) controllers.get(ScannedController.class);
        assertThat(controller.message()).isEqualTo("scanned");
    }

    @Test
    @DisplayName("중복된 패키지에서 발견한 컨트롤러는 한 번만 생성한다")
    void create_each_controller_once() {
        final var scanner = new ControllerScanner("scanner", "scanner.samples");
        final var before = ScannedController.constructionCount;

        final var controllers = scanner.scan();

        assertThat(controllers).hasSize(1);
        assertThat(ScannedController.constructionCount - before).isEqualTo(1);
    }

    @Test
    @DisplayName("기본 생성자가 없으면 실패한 컨트롤러 타입과 원인 예외를 전달한다")
    void preserve_missing_constructor_failure() {
        final var scanner = new ControllerScanner("invalidcontrollers.missing");

        assertThatThrownBy(scanner::scan)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Failed to instantiate controller: " + MissingDefaultConstructorController.class.getName())
                .hasCauseInstanceOf(NoSuchMethodException.class);
    }

    @Test
    @DisplayName("생성자 실행이 실패하면 컨트롤러 타입과 전체 예외 원인을 유지한다")
    void preserve_constructor_execution_failure() {
        final var scanner = new ControllerScanner("invalidcontrollers.throwing");

        assertThatThrownBy(scanner::scan)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Failed to instantiate controller: " + ThrowingController.class.getName())
                .hasCauseInstanceOf(InvocationTargetException.class)
                .satisfies(exception -> assertThat(exception.getCause().getCause()).isSameAs(ThrowingController.FAILURE));
    }

}
