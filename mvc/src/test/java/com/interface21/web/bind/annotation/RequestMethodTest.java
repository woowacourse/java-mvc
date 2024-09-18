package com.interface21.web.bind.annotation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.NoSuchElementException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RequestMethodTest {

    @DisplayName("존재하는 메서드 이름이면 그 값을 반환한다.")
    @Test
    void givenName_WhenExist_thenCorrect() {
        RequestMethod requestMethod = RequestMethod.of("GET");

        assertThat(requestMethod).isEqualTo(RequestMethod.GET);
    }

    @DisplayName("존재하지 않는 메서드 이름이면 예외를 발생시킨다.")
    @Test
    void givenName_WhenDoesNotExist_thenThrowException() {
        assertThatThrownBy(() -> RequestMethod.of("INVALID"))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("지원하지 않는 메소드(INVALID)입니다.");
    }
}
