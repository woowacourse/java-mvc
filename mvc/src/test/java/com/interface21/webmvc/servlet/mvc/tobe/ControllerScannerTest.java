package com.interface21.webmvc.servlet.mvc.tobe;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import samples.TestController;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("컨트롤러 탐색")
class ControllerScannerTest {

    @Test
    @DisplayName("지정한 패키지에서 @Controller가 붙은 클래스만 찾는다")
    void findsOnlyAnnotatedControllers() {
        // given
        final var scanner = new ControllerScanner("samples");

        // when
        final var controllers = scanner.getControllers();

        // then
        assertThat(controllers.keySet()).containsExactly(TestController.class);
    }

    @Test
    @DisplayName("찾은 컨트롤러 클래스의 객체를 생성한다")
    void instantiatesController() {
        // given
        final var scanner = new ControllerScanner("samples");

        // when
        final var controllers = scanner.getControllers();

        // then
        assertThat(controllers.get(TestController.class)).isInstanceOf(TestController.class);
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("repeatedOrOverlappingScopes")
    @DisplayName("탐색 범위가 반복되거나 겹쳐도 같은 컨트롤러 클래스는 한 번만 등록한다")
    void registersControllerOnceForRepeatedOrOverlappingScopes(final String scopeName, final Object[] scopes) {
        // given
        final var scanner = new ControllerScanner(scopes);

        // when
        final var controllers = scanner.getControllers();

        // then
        assertThat(controllers.keySet()).containsExactly(TestController.class);
    }

    private static Stream<Arguments> repeatedOrOverlappingScopes() {
        return Stream.of(
                Arguments.of("동일한 패키지 반복: samples, samples", new Object[]{"samples", "samples"}),
                Arguments.of("패키지와 클래스 기준 범위 중첩: samples, TestController.class",
                        new Object[]{"samples", TestController.class})
        );
    }
}
