package com.interface21.web.bind.annotation;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("요청 메서드")
class RequestMethodTest {

    @DisplayName("요청 메서드는 존재하지 않는 메서드를 찾을 시 오류를 반환한다.")
    @Test
    void findByInvalidName() {
        // given
        String invalidName = "GETS";

        // when & then
        assertThatThrownBy(() -> RequestMethod.findByName(invalidName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(invalidName + "에 해당하는 HTTP 메서드가 없습니다.");
    }
}
