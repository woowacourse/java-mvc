package com.interface21.webmvc.servlet.mvc.tobe;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AnnotationHandlerRegistrationFailureTest {

    @Test
    @DisplayName("같은 경로와 HTTP 메서드가 중복되면 초기화에 실패한다")
    void rejectsDuplicateMappings() {
        // given
        final var handlerMapping = new AnnotationHandlerMapping("duplicatemappingfixtures");

        // when & then
        assertThatThrownBy(handlerMapping::initialize)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("중복된 요청 매핑", "/duplicate", "GET");
    }

    @DisplayName("컨트롤러 메서드 규약을 위반하면 요청을 받기 전에 초기화에 실패한다")
    @ParameterizedTest(name = "[{index}] {0}")
    @CsvSource({
            "잘못된 인자, invalidmappingfixtures.parameters, 인자",
            "잘못된 반환 타입, invalidmappingfixtures.returntype, 반환 타입"
    })
    void rejectsInvalidSignatureDuringInitialization(
            final String scenario, final String basePackage, final String expectedMessage) {
        // given
        final var handlerMapping = new AnnotationHandlerMapping(basePackage);

        // when & then
        assertThatThrownBy(handlerMapping::initialize)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(expectedMessage, "handle");
    }
}
