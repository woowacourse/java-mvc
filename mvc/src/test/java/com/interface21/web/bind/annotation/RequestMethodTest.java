package com.interface21.web.bind.annotation;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메서드 테스트")
class RequestMethodTest {

    @DisplayName("HTTP 메서드 탐색 성공 테스트")
    @Test
    void ofMethods() {
        // given
        String[] methods = {"GET", "HEAD", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "TRACE"};

        // when & then
        Arrays.stream(methods).forEach(method -> {
            RequestMethod requestMethod = RequestMethod.of(method);
            assertEquals(RequestMethod.valueOf(method), requestMethod);
        });
    }

    @Test
    @DisplayName("유효하지 않은 HTTP 메서드 입력 시 예외 발생 테스트")
    void invalidMethod() {
        // given
        String invalidMethod = "INVALID";

        // when & then
        assertThatCode(() -> RequestMethod.of(invalidMethod))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid HTTP method: INVALID");
    }
}
