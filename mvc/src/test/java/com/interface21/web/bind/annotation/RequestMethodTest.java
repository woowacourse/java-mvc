package com.interface21.web.bind.annotation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RequestMethodTest {
    private static Stream<Arguments> methodsProvider() {
        return Stream.of(
                Arguments.of("get", RequestMethod.GET),
                Arguments.of("GET", RequestMethod.GET)
        );
    }

    @DisplayName("대소문자 여부와 관계없이 메서드 이름으로 RequestMethod를 찾는다.")
    @ParameterizedTest
    @MethodSource("methodsProvider")
    void find(String method, RequestMethod requestMethod) {
        assertThat(RequestMethod.find(method)).isEqualTo(requestMethod);
    }

    @DisplayName("대소문자 여부와 관계없이 메서드 이름으로 RequestMethod를 찾는다.")
    @Test
    void cannotFind() {
        assertThatThrownBy(() -> RequestMethod.find("UNKNOWN"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Cannot find method: UNKNOWN");
    }
}
