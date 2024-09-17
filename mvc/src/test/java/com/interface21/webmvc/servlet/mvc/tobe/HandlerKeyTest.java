package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMethod;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

class HandlerKeyTest {
    @Test
    @DisplayName("Request 에서 URI 와 Method 를 통해 생성한다.")
    void create_with_request() {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/test");
        request.setMethod("GET");

        final HandlerKey key1 = new HandlerKey(request);
        final HandlerKey key2 = new HandlerKey("/test", RequestMethod.GET);
        assertThat(key1).isEqualTo(key2);
    }
}
