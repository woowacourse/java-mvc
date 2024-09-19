package com.interface21.web.bind.annotation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class RequestMethodTest {

    @DisplayName("문자열로 RequestMethod를 얻을 수 있다.")
    @ParameterizedTest
    @ValueSource(strings = {"get", "GET", "gEt"})
    void from(String rawMethod) {
        RequestMethod method = RequestMethod.from(rawMethod);
        assertThat(method).isEqualTo(RequestMethod.GET);
    }

    @DisplayName("존재하지 않는 메서드 입력 시 예외 발생")
    @Test
    void from_ThrowsException() {
        assertThatThrownBy(() -> RequestMethod.from("REMOVE"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 HTTP 메서드입니다.");
    }
}
