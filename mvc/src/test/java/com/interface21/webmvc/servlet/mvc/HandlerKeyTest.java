package com.interface21.webmvc.servlet.mvc;

import static org.assertj.core.api.Assertions.assertThat;

import com.interface21.web.bind.annotation.RequestMethod;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerKeyTest {

    @DisplayName("같은 URL과 RequestMethod를 가진 두 HandlerKey는 동등하다.")
    @Test
    void equals_and_hashCode_same() {
        // given
        final HandlerKey key1 = new HandlerKey("/test", RequestMethod.GET);
        final HandlerKey key2 = new HandlerKey("/test", RequestMethod.GET);

        // when & then
        assertThat(key1)
                .isEqualTo(key2)
                .hasSameHashCodeAs(key2);
    }

    @DisplayName("URL이 다르면 두 HandlerKey는 동등하지 않다.")
    @Test
    void equals_differentUrl() {
        // given
        final HandlerKey key1 = new HandlerKey("/test1", RequestMethod.GET);
        final HandlerKey key2 = new HandlerKey("/test2", RequestMethod.GET);

        // when & then
        assertThat(key1).isNotEqualTo(key2);
    }

    @DisplayName("RequestMethod가 다르면 두 HandlerKey는 동등하지 않다.")
    @Test
    void equals_differentMethod() {
        // given
        final HandlerKey key1 = new HandlerKey("/test", RequestMethod.GET);
        final HandlerKey key2 = new HandlerKey("/test", RequestMethod.POST);

        // when & then
        assertThat(key1).isNotEqualTo(key2);
    }

    @DisplayName("다른 타입의 객체나 null과 비교하면 동등하지 않다.")
    @Test
    void equals_withNullOrDifferentType() {
        // given
        final HandlerKey key1 = new HandlerKey("/test", RequestMethod.GET);

        // when & then
        assertThat(key1.equals(new Object())).isFalse();
    }
}
