package com.interface21.web.bind.annotation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class RequestMethodTest {

    @Test
    void parse() {
        // given
        String stringRequestMethod = "post";

        // when
        RequestMethod requestMethod = RequestMethod.parse(stringRequestMethod);

        // then
        assertThat(requestMethod).isEqualTo(RequestMethod.POST);
    }
}
