package com.interface21.web.bind.annotation;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RequestMethodTest {

    @Test
    @DisplayName("올바른 HTTP Method 문자열이 주어지면, 해당 RequestMethod 를 반환한다.")
    void givenCorrectString_thenReturnEnum() {
        assertAll(
                () -> assertEquals(RequestMethod.GET, RequestMethod.from("GET")),
                () -> assertEquals(RequestMethod.HEAD, RequestMethod.from("HEAD")),
                () -> assertEquals(RequestMethod.POST, RequestMethod.from("POST")),
                () -> assertEquals(RequestMethod.PUT, RequestMethod.from("PUT")),
                () -> assertEquals(RequestMethod.PATCH, RequestMethod.from("PATCH")),
                () -> assertEquals(RequestMethod.DELETE, RequestMethod.from("DELETE")),
                () -> assertEquals(RequestMethod.OPTIONS, RequestMethod.from("OPTIONS")),
                () -> assertEquals(RequestMethod.TRACE, RequestMethod.from("TRACE"))
        );
    }

    @Test
    @DisplayName("올바르지 않은 HTTP Method 문자열이 주어지면, 예외를 던진다.")
    void givenIncorrectString_thenThrowIllegalArgumentException() {
        assertThatCode(() -> RequestMethod.from("WRONG"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Wrong http method - given: WRONG");
    }
}
