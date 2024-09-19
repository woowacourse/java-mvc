package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import com.interface21.web.bind.annotation.RequestMethod;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerKeyTest {

    @Test
    @DisplayName("동등성에 대해 검증한다.")
    void methodName() {
        // given
        HandlerKey key1 = new HandlerKey("/api/test", RequestMethod.GET);
        HandlerKey key2 = new HandlerKey("/api/test", RequestMethod.GET);

        // when & then
        assertThat(key1).isEqualTo(key2);
    }
}
