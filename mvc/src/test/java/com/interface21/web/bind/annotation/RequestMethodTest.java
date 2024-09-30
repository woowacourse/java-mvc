package com.interface21.web.bind.annotation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import static com.interface21.web.bind.annotation.RequestMethod.GET;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RequestMethodTest {

    @Test
    @DisplayName("이름과 동일한 값을 반환한다.")
    void getRequestMethod() {
        var name = "GET";

        assertThat(RequestMethod.of(name)).isEqualTo(GET);
    }

    @Test
    @DisplayName("해당하는 값이 없으면 null을 반환한다.")
    void getRequestMethodNull() {
        var name = "GEET";

        assertThat(RequestMethod.of(name)).isNull();
    }
}
