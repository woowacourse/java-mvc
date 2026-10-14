package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMethod;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

class HandlerKeyTest {


    @DisplayName("URL과 HTTP 메서드가 같으면 다른 객체여도 같은 키다")
    @Test
    void equalsByValue() {
        final var first = new HandlerKey("/users", RequestMethod.GET);
        final var second = new HandlerKey("/users", RequestMethod.GET);

        assertThat(first).isNotSameAs(second);
        assertThat(first).isEqualTo(second);
        assertThat(first).hasSameHashCodeAs(second);
    }

    @DisplayName("HTTP 메서드가 다르면 다른 키다")
    @Test
    void differentMethodIsDifferentKey() {
        assertThat(new HandlerKey("/users", RequestMethod.GET))
                .isNotEqualTo(new HandlerKey("/users", RequestMethod.POST));
    }

    @DisplayName("새로 만든 키로도 Map에 저장된 값을 찾을 수 있다")
    @Test
    void worksAsMapKey() {
        final var map = new HashMap<HandlerKey, String>();
        map.put(new HandlerKey("/users", RequestMethod.GET), "handler");

        assertThat(map.get(new HandlerKey("/users", RequestMethod.GET))).isEqualTo("handler");
    }
}