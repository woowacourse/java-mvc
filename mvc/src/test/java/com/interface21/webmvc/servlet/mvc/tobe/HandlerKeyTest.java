package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.handler.HandlerKey;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

class HandlerKeyTest {

    @Test
    @DisplayName("HandlerKey equals and hashCode test")
    void equalsAndHashCode() {
        final var handlerKey1 = new HandlerKey("/get-test", RequestMethod.GET);
        final var handlerKey2 = new HandlerKey("/get-test", RequestMethod.GET);
        final var handlerKey3 = new HandlerKey("/post-test", RequestMethod.POST);

        assertSoftly(softly -> {
                    softly.assertThat(handlerKey1).isEqualTo(handlerKey2);
                    softly.assertThat(handlerKey1).hasSameHashCodeAs(handlerKey2);
                    softly.assertThat(handlerKey1).isNotEqualTo(handlerKey3);
                    softly.assertThat(handlerKey1).doesNotHaveSameHashCodeAs(handlerKey3);
                }
        );
    }
}
