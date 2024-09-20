package com.interface21.web.bind.annotation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RequestMethodTest {

    @DisplayName("전달받은 문자열과 형태가 같은 요청 메서드를 반환한다.")
    @Test
    void should_findRequestMethod_when_givenValidString() {
        // given
        String raw = "GET";

        // when
        RequestMethod requestMethod = RequestMethod.find(raw);

        // then
        assertThat(requestMethod).isEqualTo(RequestMethod.GET);
    }

    @DisplayName("전달받은 문자열이 유효하지 않은 경우 예외가 발생한다.")
    @Test
    void should_throwException_when_givenInvalidString() {
        // given
        String raw = "INVALID";

        // when & then
        assertThatThrownBy(() -> RequestMethod.find(raw))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("유효하지 않은 요청 메서드입니다: " + raw);
    }
}