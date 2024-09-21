package com.interface21.web.bind.annotation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

class RequestMethodTest {
    private static Stream<Arguments> methodsProvider() {
        return Stream.of(
                Arguments.of("get", RequestMethod.GET),
                Arguments.of("UNKNOWN", RequestMethod.GET)
        );
    }

    @DisplayName("대소문자를 구분하여 메서드 이름으로 RequestMethod를 찾는다.")
    @Test
    void find() {
        List<String> names = List.of("GET", "HEAD", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "TRACE");
        assertAll(
                () -> names.forEach(name -> assertThat(RequestMethod.find(name).name()).isEqualTo(name))
        );
    }

    @DisplayName("대소문자를 구분하여 메서드 이름으로 RequestMethod를 찾는다.")
    @ParameterizedTest
    @ValueSource(strings = {"GET", "HEAD", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "TRACE"})
    void find(String name) {
        assertThat(RequestMethod.find(name).name()).isEqualTo(name);
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
