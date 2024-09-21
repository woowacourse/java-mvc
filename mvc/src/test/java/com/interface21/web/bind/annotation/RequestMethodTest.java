package com.interface21.web.bind.annotation;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class RequestMethodTest {

    @DisplayName("RFC 형식에 맞는 HTTP METHOD 면 생성된다.")
    @ValueSource(strings = {"GET", "HEAD", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "TRACE"})
    @ParameterizedTest
    void from(String name) {
        assertThatCode(() -> RequestMethod.from(name))
                .doesNotThrowAnyException();
    }

    @DisplayName("RFC 형식에 맞지 않는 HTTP METHOD 면 생성된다.")
    @NullAndEmptySource
    @ValueSource(strings = {"Get", "post"})
    @ParameterizedTest
    void from_fail(String name) {
        assertThatThrownBy(() -> RequestMethod.from(name))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
