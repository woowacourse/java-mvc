package com.interface21.web.bind.annotation;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RequestMethodTest {

    @DisplayName("존재하지 않는 HTTP Request Method는 지원하지 않는다.")
    @Test
    void fromInvalidMethod() {
        assertThatThrownBy(() -> RequestMethod.from(""))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
