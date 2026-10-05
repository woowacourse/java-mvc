package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMethod;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class HandlerKeyTest {

    @Test
    void equivalentUnrestrictedKeyFindsSameMapping() {
        final var handlers = Map.of(HandlerKey.anyMethod("/test"), "handler");

        assertThat(handlers.get(HandlerKey.anyMethod("/test"))).isEqualTo("handler");
        assertThat(handlers.get(new HandlerKey("/test", RequestMethod.GET))).isNull();
        assertThat(handlers.get(HandlerKey.anyMethod("/other"))).isNull();
    }
}
