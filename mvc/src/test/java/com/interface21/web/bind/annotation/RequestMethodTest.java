package com.interface21.web.bind.annotation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RequestMethodTest {

    @ParameterizedTest
    @ValueSource(strings = {"no", "pso", "ajbd"})
    @DisplayName("존재하지 않는 Method를 찾는 경우 예외를 발생한다.")
    void findMethod_WhenNoExistMethod(String method) {
        assertThatThrownBy(() -> RequestMethod.findMethod(method))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Method가 소문자로 이뤄진 경우에도 찾는다.")
    void findMethod_WhenLowerCase() {
        RequestMethod requestMethod = RequestMethod.findMethod("get");

        assertThat(requestMethod).isEqualTo(RequestMethod.GET);
    }
}
