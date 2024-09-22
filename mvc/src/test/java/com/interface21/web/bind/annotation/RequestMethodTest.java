package com.interface21.web.bind.annotation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class RequestMethodTest {

    @DisplayName("전달받은 문자열과 완전히 일치하는 RequestMethod를 반환한다.")
    @EnumSource(RequestMethod.class)
    @ParameterizedTest
    void findRequestMethod(RequestMethod expected) {
        String given = expected.name();

        assertThat(RequestMethod.of(given)).isEqualTo(expected);
    }

    @DisplayName("지원하지 않는 메서드를 찾으려고 시도하면 예외가 발생한다.")
    @Test
    void findRequestMethodWithInvalidValue() {
        String given = "INVALID";

        assertThatIllegalArgumentException()
                .isThrownBy(() -> RequestMethod.of(given))
                .withMessage("지원하지 않는 메서드입니다.");
    }
}
