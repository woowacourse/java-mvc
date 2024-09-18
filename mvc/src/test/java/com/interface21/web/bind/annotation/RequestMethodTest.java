package com.interface21.web.bind.annotation;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RequestMethodTest {

    static Stream<Arguments> methodAndNameProvider() {
        return Stream.of(
                Arguments.of(
                        RequestMethod.GET,
                        "GET"
                ),
                Arguments.of(
                        RequestMethod.GET,
                        "get"
                ),
                Arguments.of(
                        RequestMethod.GET,
                        "Get"
                )
        );
    }

    @DisplayName("대소문자 구분없이 HttpMethod 이름과 대응하는 RequestMethod을 찾는다.")
    @ParameterizedTest
    @MethodSource("methodAndNameProvider")
    void getMethod(RequestMethod method, String name) {
        // when
        RequestMethod found = RequestMethod.getMethod(name);

        // then
        assertThat(found).isEqualTo(method);
    }
}
