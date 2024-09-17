package com.interface21.web.bind.annotation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RequestMethodTest {

    @Test
    @DisplayName("메소드 명은 대소문자를 구분하지 않는다.")
    void some() {
        final RequestMethod method = RequestMethod.GET;
        assertThat(RequestMethod.from("get")).isEqualTo(method);
        assertThat(RequestMethod.from("GET")).isEqualTo(method);
    }

    @Test
    @DisplayName("없는 메소드 명은 예외를 발생한다.")
    void some1() {
        assertThatThrownBy(() -> RequestMethod.from("SOME"))
                .isInstanceOf(NotSupportMethodException.class);
    }
}
