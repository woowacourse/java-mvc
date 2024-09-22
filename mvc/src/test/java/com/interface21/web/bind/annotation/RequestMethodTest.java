package com.interface21.web.bind.annotation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RequestMethodTest {

    @DisplayName("성공 : Http Method 명으로 method를 찾을 수 있다")
    @Test
    void getRequestMethod_ByMethodName() {
        String requestMethod = "GET";
        RequestMethod expectedMethod = RequestMethod.GET;

        assertThat(RequestMethod.from(requestMethod)).isEqualTo(expectedMethod);
    }

    @DisplayName("실패 : 존재하지 않는 Http Method를 탐색시 NoSuchMethodError를 반환한다")
    @Test
    void throwNoSuchMethodError_WhenCanNotFindMethodName() {
        String requestMethod = "noSuchMethod";

        assertThatThrownBy(() -> RequestMethod.from(requestMethod))
                .isInstanceOf(NoSuchMethodError.class);
    }
}
