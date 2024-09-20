package com.interface21.web.bind.annotation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

class RequestMethodTest {
    private static Stream<Arguments> methodsProvider() {
        return Stream.of(
                Arguments.of("get", RequestMethod.GET),
                Arguments.of("UNKNOWN", RequestMethod.GET)
        );
    }

    @DisplayName("대소문자를 구분하여 메서드 이름으로 RequestMethod를 찾는다.")
    @ParameterizedTest
    @EnumSource(RequestMethod.class)
    void find(RequestMethod requestMethod) {
        assertThat(RequestMethod.find(requestMethod.name())).isEqualTo(requestMethod);
    }

    @DisplayName("대소문자 여부와 관계없이 메서드 이름으로 RequestMethod를 찾는다.")
    @ParameterizedTest
    @MethodSource("methodsProvider")
    void cannotFind(String requestMethod) {
        assertThatThrownBy(() -> RequestMethod.find(requestMethod))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Cannot find method: " + requestMethod);
    }
}
