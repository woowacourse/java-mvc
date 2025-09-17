package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import samples.scanner.pass.TestController;

/**
 * 샘플 패키지를 구성하여 컨트롤러 스캐너를 테스트합니다.
 *
 * @see ControllerScanner
 * @see samples.scanner.pass.TestController
 * @see samples.scanner.fail.ControllerWithNoDefaultConstructor
 */
class ControllerScannerTest {

    private ControllerScanner controllerScanner;

    @BeforeEach
    void setUp() {
        controllerScanner = new ControllerScanner();
    }

    @DisplayName("samples.scanner.pass 패키지를 스캔하면 TestController 클래스를 인스턴스화한다.")
    @Test
    void scan() {
        // given
        final String scanPackage = "samples.scanner.pass";

        // when
        final Map<Class<?>, Object> controllers = controllerScanner.scan(scanPackage);

        // then
        assertAll(
                () -> assertThat(controllers).hasSize(1),
                () -> assertThat(controllers.get(TestController.class)).isInstanceOf(TestController.class)
        );
    }

    @DisplayName("컨트롤러가 없는 패키지를 스캔하면 빈 맵을 반환한다.")
    @Test
    void scanEmpty() {
        // given
        final String scanPackage = "samples.scanner.empty";

        // when
        final Map<Class<?>, Object> controllers = controllerScanner.scan(scanPackage);

        // then
        assertThat(controllers).isEmpty();
    }

    @DisplayName("기본 생성자가 없는 컨트롤러를 스캔하면 예외가 발생한다.")
    @Test
    void scanFail() {
        // given
        final String scanPackage = "samples.scanner.fail";

        // when & then
        assertThatThrownBy(() -> controllerScanner.scan(scanPackage))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("기본 생성자가 없는 컨트롤러는 인스턴스화할 수 없습니다.");
    }

    @DisplayName("패키지 경로가 null이거나 비어있으면 예외가 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void scanNullOrEmpty(final Object[] basePackage) {
        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> controllerScanner.scan(basePackage))
                .withMessage("basePackage는 null이거나 비어 있을 수 없습니다.");
    }
}
