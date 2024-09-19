package com.interface21.web.bind.annotation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class RequestMethodTest {

    @DisplayName("존재하지 않는 HTTP 메서드 이름을 전달하면 예외가 발생한다.")
    @Test
    void throwsWhenInvalidHttpMethodName() {
        // given
        String invalidHttpMethodName = "INVALID";

        // when & then
        assertThatThrownBy(() -> RequestMethod.from(invalidHttpMethodName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(invalidHttpMethodName + "은(는) 올바른 HTTP 메서드가 아닙니다.");
    }

    @DisplayName("HTTP 메서드 이름으로 RequestMethod 객체를 반환한다.")
    @ParameterizedTest
    @EnumSource(RequestMethod.class)
    void from(RequestMethod requestMethod) {
        // given
        String httpMethodName = requestMethod.name();

        // when & then
        assertThat(RequestMethod.from(httpMethodName)).isEqualTo(requestMethod);
    }
}
