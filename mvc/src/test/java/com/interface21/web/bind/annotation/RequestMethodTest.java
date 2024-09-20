package com.interface21.web.bind.annotation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

public class RequestMethodTest {

    @DisplayName("문자열과 일치하는 RequestMethod를 반환한다.")
    @EnumSource(RequestMethod.class)
    @ParameterizedTest
    void ofTest(final RequestMethod expected) {
        // given
        final String methodName = expected.name();

        // when
        final RequestMethod actual = RequestMethod.of(methodName);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("문자열과 일치하는 RequestMethod가 없으면 IllegalArgumentException을 던진다.")
    @Test
    void ofTestThrowExceptionWhenNotFound() {
        // given
        final String methodName = "EXAMPLE";

        // when & then
        assertThatThrownBy(() -> RequestMethod.of(methodName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("지원하지 않는 HTTP Method 입니다.");
    }
}
