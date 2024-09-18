package com.interface21.web.bind.annotation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RequestMethodTest {

    @Test
    @DisplayName("test")
    void valueOfName() {
        String name = "GET";

        assertThat(RequestMethod.valueOfName(name)).isEqualTo(RequestMethod.GET);
    }

    @Test
    @DisplayName("test")
    void valueOfNameException() {
        String name = "GETT";

        assertThatThrownBy(() -> RequestMethod.valueOfName(name))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
