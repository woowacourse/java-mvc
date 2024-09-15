package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import com.interface21.web.bind.annotation.RequestMethod;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

class HandlerKeyTest {

    @DisplayName("Request의 url과 method로 HandlerKey를 생성한다.")
    @Test
    void of() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/login");

        HandlerKey handlerKey = HandlerKey.of(request);

        assertThat(handlerKey).isEqualTo(new HandlerKey("/login", RequestMethod.GET));
    }
}
