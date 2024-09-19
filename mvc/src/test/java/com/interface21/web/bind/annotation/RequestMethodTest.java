package com.interface21.web.bind.annotation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("Http 메서드 테스트")
class RequestMethodTest {

    @DisplayName("문자열이 주어졌을 때 문자열에 맞는 Http 메서드를 얻어올 수 있다")
    @ParameterizedTest
    @CsvSource({
            "GET, GET",
            "POST, POST",
            "PUT, PUT",
            "PATCH, PATCH",
            "DELETE, DELETE",
            "HEAD, HEAD",
            "OPTIONS, OPTIONS",
            "TRACE, TRACE"
    })
    void from(String httpMethod, RequestMethod expectedMethod) {
        assertThat(RequestMethod.from(httpMethod)).isEqualTo(expectedMethod);
    }
}
