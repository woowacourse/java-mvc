package com.interface21.web.bind.annotation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class RequestMethodTest {

    @ParameterizedTest
    @ValueSource(strings = {"GET", "POST", "PUT", "PATCH", "DELETE"})
    void fromTest(String method) {
        RequestMethod expected = RequestMethod.valueOf(method);

        RequestMethod actual = RequestMethod.from(method);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void fromTest_whenNotExistMethod_throwException() {
        assertThatThrownBy(() -> RequestMethod.from("NOT_EXIST"))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 메서드가 존재하지 않습니다.");
    }
}
